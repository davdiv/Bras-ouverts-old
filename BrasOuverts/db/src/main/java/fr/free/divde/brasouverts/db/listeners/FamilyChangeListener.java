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
import fr.free.divde.brasouverts.db.model.Family;
import org.jdesktop.observablecollections.ObservableList;

public class FamilyChangeListener extends ChangeListener {

    @Override
    protected boolean includeSubObject(DBObject object, String property) {
        if (object instanceof Family) {
            return Family.PROP_ADDRESS.equals(property);
        }
        return false;
    }

    @Override
    protected boolean includeCollectionObjects(ObservableList<? extends DBObject> list) {
        return true;
    }

    @Override
    protected void addSubObjects(DBObject object) {
        if (object instanceof Family) {
            Family family = (Family) object;
            listenTo(family.getAddress());
            listenTo(family.getPeople());
            listenTo(family.getResources());
            listenTo(family.getVisits());
        }
    }

    @Override
    protected void removeSubObjects(DBObject object) {
        if (object instanceof Family) {
            Family family = (Family) object;
            stopListeningTo(family.getAddress());
            stopListeningTo(family.getPeople());
            stopListeningTo(family.getResources());
            stopListeningTo(family.getVisits());
        }
    }
}
