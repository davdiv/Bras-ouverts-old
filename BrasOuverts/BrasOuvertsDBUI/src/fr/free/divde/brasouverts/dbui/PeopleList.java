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
package fr.free.divde.brasouverts.dbui;

import fr.free.divde.brasouverts.dbui.uicomponents.JXXTable;
import fr.free.divde.brasouverts.dbui.uicomponents.ComboBoxCellEditor;
import fr.free.divde.brasouverts.dbui.uicomponents.DateCellEditor;
import fr.free.divde.brasouverts.dbui.uicomponents.StringCellEditor;
import fr.free.divde.brasouverts.db.model.Country;
import fr.free.divde.brasouverts.db.model.Gender;
import fr.free.divde.brasouverts.db.model.Person;
import fr.free.divde.brasouverts.dbui.uicomponents.AgeConverter;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

public class PeopleList extends JXXTable<Person> {

    @Getter
    protected List<Country> countries = Arrays.asList((Country) null);
    public static String PROP_COUNTRIES = "countries";

    public void setCountries(List<Country> newCountries) {
        firePropertyChange(PROP_COUNTRIES, countries, countries = newCountries);
    }

    @Override
    protected Column[] createColumns() {
        Column age = new Column("PeopleList.Age", Person.PROP_BIRTHDATE, Integer.class, null, 70);
        age.setConverter(new AgeConverter());
        age.setEditable(false);
        Column[] res = new JXXTable.Column[]{
            new Column("PeopleList.LastName", Person.PROP_LASTNAME, String.class, new StringCellEditor(), 150),
            new Column("PeopleList.FirstName", Person.PROP_FIRSTNAME, String.class, new StringCellEditor(), 150),
            new Column("PeopleList.Gender", Person.PROP_GENDER, Gender.class, new ComboBoxCellEditor(Arrays.asList(null, Gender.MALE, Gender.FEMALE)), 100),
            // TODO: bind the countries property instead of only accessing it
            new Column("PeopleList.BirthCountry", Person.PROP_BIRTHCOUNTRY, Country.class, new ComboBoxCellEditor(countries), 150),
            new Column("PeopleList.BirthDate", Person.PROP_BIRTHDATE, java.util.Date.class, new DateCellEditor(), 150),
            age,
            new Column("PeopleList.Comments", Person.PROP_COMMENT, String.class, new StringCellEditor(), 150)
        };
        return res;
    }
}
