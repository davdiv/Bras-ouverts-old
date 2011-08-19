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
package fr.free.divde.brasouverts.db.delete;

import fr.free.divde.brasouverts.db.DBReadWriteSession;
import fr.free.divde.brasouverts.db.model.Family;
import fr.free.divde.brasouverts.db.model.Person;
import fr.free.divde.brasouverts.db.model.Resource;
import fr.free.divde.brasouverts.db.model.Visit;
import fr.free.divde.brasouverts.db.visitors.FamilyVisitor;
import fr.free.divde.brasouverts.db.visitors.PersonVisitor;
import fr.free.divde.brasouverts.db.visitors.ResourceVisitor;
import fr.free.divde.brasouverts.db.visitors.VisitVisitor;

public class DeleteFamily implements FamilyVisitor, ResourceVisitor, VisitVisitor, PersonVisitor {
    
    private DBReadWriteSession dbSession;
    
    public DeleteFamily(DBReadWriteSession dbSession) {
        this.dbSession = dbSession;
    }
    
    @Override
    public void visit(Family family) {
        dbSession.remove(family);
    }
    
    @Override
    public void visit(Resource resource) {
        dbSession.remove(resource);
    }
    
    @Override
    public void visit(Visit visit) {
        dbSession.remove(visit);
    }
    
    @Override
    public void visit(Person person) {
        dbSession.remove(person);
    }
}
