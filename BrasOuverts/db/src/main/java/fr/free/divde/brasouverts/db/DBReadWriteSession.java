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
package fr.free.divde.brasouverts.db;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import lombok.Getter;

public class DBReadWriteSession extends DBReadOnlySession {

    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);

    public void removePropertyChangeListener(PropertyChangeListener pl) {
        propertyChange.removePropertyChangeListener(pl);
    }

    public void addPropertyChangeListener(PropertyChangeListener pl) {
        propertyChange.addPropertyChangeListener(pl);
    }
    @Getter
    private boolean changed = false;
    public static final String PROP_CHANGED = "changed";

    private void setChanged(boolean newValue) {
        if (newValue != changed) {
            propertyChange.firePropertyChange(PROP_CHANGED, changed, changed = newValue);
        }
    }

    public void notifyChanged() {
        setChanged(true);
    }

    public void persist(Object o) {
        entityManager.persist(o);
        notifyChanged();
    }

    public void remove(Object o) {
        entityManager.remove(o);
        notifyChanged();
    }

    public void save() {
        entityManager.getTransaction().begin();
        entityManager.flush();
        entityManager.getTransaction().commit();
        setChanged(false);
    }
}
