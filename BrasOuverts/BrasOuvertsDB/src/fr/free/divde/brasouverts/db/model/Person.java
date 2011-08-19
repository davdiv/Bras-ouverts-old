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

import fr.free.divde.brasouverts.db.DBReadOnlySession;
import fr.free.divde.brasouverts.db.check.Message.MessageType;
import fr.free.divde.brasouverts.db.check.ModelCheck;
import fr.free.divde.brasouverts.db.visitors.PersonVisitor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import lombok.Getter;
import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.ObjectTypeConverter;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;

@Entity
public class Person extends DBExpandableEntity<Long> {

    public static List<Person> findPeople(DBReadOnlySession dbSession, String search) {
        TypedQuery<Person> query;
        if (search.isEmpty()) {
            query = dbSession.createQuery("SELECT p FROM Person p", Person.class);
        } else {
            query = dbSession.createQuery("SELECT p FROM Person p WHERE p.firstName LIKE :search OR p.lastName LIKE :search", Person.class);
            query.setParameter("search", search);
        }
        return query.getResultList();
    }
    private static final long serialVersionUID = 1L;
    @Getter
    private String firstName;
    public static final String PROP_FIRSTNAME = "firstName";
    @Getter
    private String lastName;
    public static final String PROP_LASTNAME = "lastName";
    public static final String PROP_GENDER = "gender";
    @ObjectTypeConverter(name = "gender", objectType = Gender.class, dataType = String.class, conversionValues = {
        @ConversionValue(objectValue = "MALE", dataValue = "M"),
        @ConversionValue(objectValue = "FEMALE", dataValue = "F")})
    @Convert("gender")
    @Getter
    private Gender gender;
    @Temporal(TemporalType.DATE)
    @Getter
    private Date birthDate;
    public static final String PROP_BIRTHDATE = "birthDate";
    @Getter
    private Country birthCountry;
    public static final String PROP_BIRTHCOUNTRY = "birthCountry";
    @ManyToOne
    @Getter
    private Family family;
    public static final String PROP_FAMILY = "family";
    @OneToMany(mappedBy = "person")
    private List<PersonalCard> personalCards = new ArrayList<PersonalCard>();
    public static final String PROP_PERSONAL_CARDS = "personalCards";
    private transient ObservableList<PersonalCard> observablePersonalCards;

    public ObservableList<PersonalCard> getPersonalCards() {
        if (observablePersonalCards == null) {
            observablePersonalCards = ObservableCollections.observableList(personalCards);
            observablePersonalCards.addObservableListListener(oneToManyObservableListListener(PROP_PERSONAL_CARDS, PersonalCard.PROP_PERSON));
        }
        return observablePersonalCards;
    }

    public void setGender(Gender newValue) {
        Gender oldValue = gender;
        gender = newValue;
        firePropertyChange(PROP_GENDER, oldValue, newValue);
    }

    public void setBirthDate(Date newValue) {
        firePropertyChange(PROP_BIRTHDATE, birthDate, birthDate = newValue);
    }

    public void setFamily(Family newValue) {
        manyToOnePropertyChange(PROP_FAMILY, Family.PROP_PEOPLE, family, family = newValue);
    }

    public void setFirstName(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(PROP_FIRSTNAME, firstName, firstName = newValue);
    }

    public void setLastName(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(PROP_LASTNAME, lastName, lastName = newValue);
    }

    public void setBirthCountry(Country newValue) {
        firePropertyChange(PROP_BIRTHCOUNTRY, birthCountry, birthCountry = newValue);
    }

    @Override
    public String toString() {
        String fName = filterNullValue(firstName);
        String lName = filterNullValue(lastName);
        return fName + " " + lName;
    }

    public void accept(PersonVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void check(ModelCheck check) {
        if (birthDate != null && birthDate.getTime() > System.currentTimeMillis()) {
            check.addMessage(this, MessageType.ERROR, Person.class, "Person.BirthDateInFuture");
        }
    }
}
