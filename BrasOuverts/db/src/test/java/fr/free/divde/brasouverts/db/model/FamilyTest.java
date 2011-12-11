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

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class FamilyTest {

    public FamilyTest() {
    }

    @Test
    public void testAddPersonThroughSetFamily() {
        Family family = new Family();
        Person person = new Person();
        List<Person> peopleInFamily = family.getPeople();

        person.setFamily(family);

        assertTrue(person.getFamily() == family);
        assertTrue(peopleInFamily.size() == 1);
        assertTrue(peopleInFamily.get(0) == person);

        person.setFamily(null);

        assertTrue(person.getFamily() == null);
        assertTrue(peopleInFamily.isEmpty());
    }

    @Test
    public void testAddPersonThroughGetPeopleAdd() {
        Family family = new Family();
        Person person = new Person();
        List<Person> peopleInFamily = family.getPeople();

        peopleInFamily.add(person);

        assertTrue(person.getFamily() == family);
        assertTrue(peopleInFamily.size() == 1);
        assertTrue(peopleInFamily.get(0) == person);

        peopleInFamily.remove(person);

        assertTrue(person.getFamily() == null);
        assertTrue(peopleInFamily.isEmpty());
    }
}
