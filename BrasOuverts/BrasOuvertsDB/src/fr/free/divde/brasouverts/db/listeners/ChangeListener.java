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
package fr.free.divde.brasouverts.db.listeners;

import fr.free.divde.brasouverts.db.model.DBObject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.ListIterator;
import lombok.Getter;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;

public abstract class ChangeListener {

    public static enum ChangeType {

        PROPERTY_CHANGE,
        COLLECTION_ADD,
        COLLECTION_REMOVE,
        COLLECTION_REPLACE
    }
    @Getter
    protected boolean bound = false;
    private IdentityHashMap<DBObject, Integer> objectsListened = new IdentityHashMap<DBObject, Integer>();
    private IdentityHashMap<ObservableList<? extends DBObject>, Integer> collectionsListened = new IdentityHashMap<ObservableList<? extends DBObject>, Integer>();

    public void setRootObject(DBObject rootObject) {
        boolean alreadyBound = bound;
        if (alreadyBound) {
            unbind();
        }
        this.rootObject = rootObject;
        if (alreadyBound) {
            bind();
        }
    }
    @Getter
    private DBObject rootObject;

    public ChangeListener() {
    }

    protected void change(Object source, ChangeType changeType) {
    }

    protected void propertyChange(PropertyChangeEvent pce) {
        change(pce.getSource(), ChangeType.PROPERTY_CHANGE);
    }

    protected void listElementsAdded(ObservableList ol, int index, int length) {
        change(ol, ChangeType.COLLECTION_ADD);
    }

    protected void listElementsRemoved(ObservableList ol, int i, List list) {
        change(ol, ChangeType.COLLECTION_REMOVE);
    }

    protected void listElementReplaced(ObservableList ol, int i, Object oldValue) {
        change(ol, ChangeType.COLLECTION_REPLACE);
    }
    private PropertyChangeListener propertyListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            Object source = pce.getSource();
            assert source instanceof DBObject;
            DBObject dbObjectSource = (DBObject) source;
            if (includeSubObject(dbObjectSource, pce.getPropertyName())) {
                Object oldValue = pce.getOldValue();
                if (oldValue instanceof DBObject) {
                    stopListeningTo((DBObject) oldValue);
                }
                Object newValue = pce.getNewValue();
                if (newValue instanceof DBObject) {
                    listenTo((DBObject) newValue);
                }
            }
            ChangeListener.this.propertyChange(pce);
        }
    };
    private ObservableListListener listListener = new ObservableListListener() {

        @Override
        public void listElementsAdded(ObservableList ol, int index, int length) {
            if (includeCollectionObjects(ol)) {
                int stopIndex = index + length;
                ListIterator it = ol.listIterator();
                while (it.hasNext() && it.nextIndex() < stopIndex) {
                    Object obj = it.next();
                    if (obj instanceof DBObject) {
                        listenTo((DBObject) obj);
                    }
                }
            }
            ChangeListener.this.listElementsAdded(ol, index, length);
        }

        @Override
        public void listElementsRemoved(ObservableList ol, int i, List list) {
            if (includeCollectionObjects(ol)) {
                for (Object obj : list) {
                    if (obj instanceof DBObject) {
                        stopListeningTo((DBObject) obj);
                    }
                }
            }
            ChangeListener.this.listElementsRemoved(ol, i, list);
        }

        @Override
        public void listElementReplaced(ObservableList ol, int i, Object oldValue) {
            if (includeCollectionObjects(ol)) {
                if (oldValue instanceof DBObject) {
                    stopListeningTo((DBObject) oldValue);
                }
                Object newValue = ol.get(i);
                if (newValue instanceof DBObject) {
                    listenTo((DBObject) newValue);
                }
            }
            ChangeListener.this.listElementReplaced(ol, i, oldValue);
        }

        @Override
        public void listElementPropertyChanged(ObservableList ol, int i) {
        }
    };

    protected abstract boolean includeSubObject(DBObject object, String property);

    protected abstract boolean includeCollectionObjects(ObservableList<? extends DBObject> list);

    protected abstract void addSubObjects(DBObject object);

    protected abstract void removeSubObjects(DBObject object);

    protected void listenTo(DBObject object) {
        if (addToMap(objectsListened, object)) {
            addSubObjects(object);
            object.addPropertyChangeListener(propertyListener);
        }
    }

    protected void stopListeningTo(DBObject object) {
        if (removeFromMap(objectsListened, object)) {
            removeSubObjects(object);
            object.removePropertyChangeListener(propertyListener);
        }
    }

    protected void listenTo(ObservableList<? extends DBObject> object) {
        if (addToMap(collectionsListened, object)) {
            if (includeCollectionObjects(object)) {
                for (DBObject subObj : object) {
                    listenTo(subObj);
                }
            }
            object.addObservableListListener(listListener);
        }
    }

    protected void stopListeningTo(ObservableList<? extends DBObject> object) {
        if (removeFromMap(collectionsListened, object)) {
            if (includeCollectionObjects(object)) {
                for (DBObject subObj : object) {
                    stopListeningTo(subObj);
                }

            }
            object.removeObservableListListener(listListener);
        }
    }

    private <T> boolean addToMap(IdentityHashMap<T, Integer> map, T object) {
        if (map.containsKey(object)) {
            Integer nb = map.get(object);
            nb += 1;
            map.put(object, nb);
            return false;

        } else {
            map.put(object, 1);
            return true;
        }
    }

    private <T> boolean removeFromMap(IdentityHashMap<T, Integer> map, T object) {
        Integer nb = map.get(object);
        if (nb == null) {
            return false;
        }
        nb -= 1;
        if (nb <= 0) {
            map.remove(object);
            return true;
        }
        map.put(object, nb);
        return false;
    }

    public void bind() {
        if (!bound) {
            bound = true;
            listenTo(rootObject);
        }
    }

    public void unbind() {
        if (bound) {
            bound = false;
            for (DBObject obj : objectsListened.keySet()) {
                obj.removePropertyChangeListener(propertyListener);
            }
            objectsListened.clear();
            for (ObservableList<? extends DBObject> list : collectionsListened.keySet()) {
                list.removeObservableListListener(listListener);
            }
            collectionsListened.clear();
        }
    }
}
