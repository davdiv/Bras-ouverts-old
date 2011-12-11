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

import java.util.Date;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;

@MappedSuperclass
public abstract class DBExpandableEntity<T> extends DBEntity<T> {

    @Getter
    private String comment;
    public static String PROP_COMMENT = "comment";
    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime = new Date();
    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastChangeTime = new Date();

    @Override
    protected boolean firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (super.firePropertyChange(propertyName, oldValue, newValue)) {
            lastChangeTime.setTime(System.currentTimeMillis());
            return true;
        }
        return false;
    }

    public void setComment(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(PROP_COMMENT, comment, comment = newValue);
    }
}
