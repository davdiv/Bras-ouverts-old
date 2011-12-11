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
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
public class PersonalCard extends DBExpandableEntity<String> {

    private static final long serialVersionUID = 1L;
    @ManyToOne
    @Getter
    private Person person;
    public static String PROP_PERSON = "person";

    public void setPerson(Person newValue) {
        manyToOnePropertyChange(PROP_PERSON, Person.PROP_PERSONAL_CARDS, person, person = newValue);
    }

    @Override
    public String toString() {
        return "fr.free.divde.brasouverts.db.model.Card[ id=" + id + " ]";
    }

    @Override
    public void check(ModelCheck check) {
    }
}
