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

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
public abstract class DBEntity<T> extends DBObject {

    public static final String PROP_ID = "id";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    protected T id;

    public void setId(T newValue) {
        firePropertyChange(PROP_ID, id, id = newValue);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            // exactly the same object
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        DBEntity other = (DBEntity) object;
        if (this.id == null || !this.id.equals(other.id)) {
            // if the id is not defined yet, objects are considered to be different,
            // unless, of course they are exactly the same reference (checked before)
            return false;
        }
        return true;
    }
}
