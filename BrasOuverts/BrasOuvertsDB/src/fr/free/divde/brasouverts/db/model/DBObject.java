/*
 * Copyright (C) 2011 divde <divde@free.fr>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.free.divde.brasouverts.db.model;

import fr.free.divde.brasouverts.db.check.ModelCheck;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;

public abstract class DBObject implements Serializable {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    protected boolean firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (oldValue != newValue) {
            propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
            return true;
        }
        return false;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected String filterEmptyValue(String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        return value;
    }

    protected String filterNullValue(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    protected <T> void manyToOnePropertyChange(String localPropName, String remotePropName, T oldValue, T newValue) {
        if (oldValue == newValue) {
            return;
        }
        BeanProperty<T, ObservableList<DBObject>> remoteProperty = BeanProperty.create(remotePropName);
        if (oldValue != null) {
            // remove all reference to this object in the remote property of the other object
            Iterator<DBObject> listIterator = remoteProperty.getValue(oldValue).iterator();
            while (listIterator.hasNext()) {
                if (listIterator.next() == this) {
                    listIterator.remove(); // note that this can trigger listeners
                }
            }
        }
        if (newValue != null) {
            // check if the item is not already present (depending on how the element was first added)
            boolean found = false;
            ObservableList<DBObject> newValueRemoteProp = remoteProperty.getValue(newValue);
            Iterator<DBObject> listIterator = newValueRemoteProp.iterator();
            while (listIterator.hasNext()) {
                if (listIterator.next() == this) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                newValueRemoteProp.add(this);
            }
        }
        propertyChangeSupport.firePropertyChange(localPropName, oldValue, newValue);
    }

    protected ObservableListListener oneToManyObservableListListener(String localPropName, String remotePropName) {
        final BeanProperty<Object, DBObject> remoteProperty = BeanProperty.create(remotePropName);
        return new ObservableListListener() {

            @Override
            public void listElementsAdded(ObservableList ol, int i, int i1) {
                int endIndex = i + i1;
                ListIterator it = ol.listIterator(i);
                while (it.hasNext() && it.nextIndex() < endIndex) {
                    remoteProperty.setValue(it.next(), DBObject.this);
                }
            }

            @Override
            public void listElementsRemoved(ObservableList ol, int i, List list) {
                // For each item removed from the list, if it still refers to this family,
                // set it to null
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    Object item = it.next();
                    if (remoteProperty.getValue(item) == DBObject.this) {
                        // this check is important because removing the item from the list
                        // may have been done because, for example, a person is moved from one family to another through setFamily
                        remoteProperty.setValue(item, null);
                    }
                }
            }

            @Override
            public void listElementReplaced(ObservableList ol, int i, Object oldElement) {
                Object newElement = ol.get(i);
                assert newElement != oldElement; // element replaced by itself
                if (remoteProperty.getValue(oldElement) == DBObject.this) {
                    remoteProperty.setValue(oldElement, null);
                }
                remoteProperty.setValue(newElement, DBObject.this);
            }

            @Override
            public void listElementPropertyChanged(ObservableList ol, int i) {
                // we cannot rely on this property
            }
        };
    }

    public abstract void check(ModelCheck check);
}
