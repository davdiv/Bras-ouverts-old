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
package fr.free.divde.brasouverts.db.check;

import fr.free.divde.brasouverts.db.model.Address;
import fr.free.divde.brasouverts.db.model.Family;
import fr.free.divde.brasouverts.db.model.Person;
import fr.free.divde.brasouverts.db.model.Resource;
import fr.free.divde.brasouverts.db.model.Visit;
import fr.free.divde.brasouverts.db.visitors.AddressVisitor;
import fr.free.divde.brasouverts.db.visitors.FamilyVisitor;
import fr.free.divde.brasouverts.db.visitors.PersonVisitor;
import fr.free.divde.brasouverts.db.visitors.ResourceVisitor;
import fr.free.divde.brasouverts.db.visitors.VisitVisitor;

public class FamilyCheck extends ModelCheck implements FamilyVisitor, AddressVisitor, PersonVisitor, ResourceVisitor, VisitVisitor {

    @Override
    public void visit(Family family) {
        family.check(this);
    }

    @Override
    public void visit(Person person) {
        person.check(this);
    }

    @Override
    public void visit(Resource resource) {
        resource.check(this);
    }

    @Override
    public void visit(Visit visit) {
        visit.check(this);
    }

    @Override
    public void visit(Address address) {
        address.check(this);
    }
}
