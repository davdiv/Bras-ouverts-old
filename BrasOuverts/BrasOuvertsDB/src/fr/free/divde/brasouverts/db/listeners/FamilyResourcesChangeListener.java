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
import fr.free.divde.brasouverts.db.model.Resource;
import java.beans.PropertyChangeEvent;
import org.jdesktop.observablecollections.ObservableList;

public class FamilyResourcesChangeListener extends ChangeListener {
    
    @Override
    protected void propertyChange(PropertyChangeEvent pce) {
        if (pce.getSource() instanceof Resource) {
            String prop = pce.getPropertyName();
            if (Resource.PROP_AMOUNT.equals(prop) || Resource.PROP_RESOURCETYPE.equals(prop) || Resource.PROP_PERIODICITY.equals(prop)) {
                // only keep notification of changes in the resources, for amount or resourceType properties
                super.propertyChange(pce);
            }
        }
    }
    
    @Override
    protected boolean includeSubObject(DBObject object, String property) {
        return false;
    }
    
    @Override
    protected boolean includeCollectionObjects(ObservableList<? extends DBObject> list) {
        Family rootObject = (Family) this.getRootObject();
        if (list == rootObject.getResources()) {
            return true;
        }
        return false;
    }
    
    @Override
    protected void addSubObjects(DBObject object) {
        if (object instanceof Family) {
            Family family = (Family) object;
            listenTo(family.getResources());
            listenTo(family.getPeople());
        }
    }
    
    @Override
    protected void removeSubObjects(DBObject object) {
        if (object instanceof Family) {
            Family family = (Family) object;
            stopListeningTo(family.getResources());
            stopListeningTo(family.getPeople());
        }
    }
}
