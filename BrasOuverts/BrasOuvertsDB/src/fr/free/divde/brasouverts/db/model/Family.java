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

import fr.free.divde.brasouverts.db.DBReadWriteSession;
import fr.free.divde.brasouverts.db.check.Message;
import fr.free.divde.brasouverts.db.check.ModelCheck;
import fr.free.divde.brasouverts.db.visitors.AddressVisitor;
import fr.free.divde.brasouverts.db.visitors.FamilyVisitor;
import fr.free.divde.brasouverts.db.visitors.PersonVisitor;
import fr.free.divde.brasouverts.db.visitors.ResourceVisitor;
import fr.free.divde.brasouverts.db.visitors.VisitVisitor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;

@Entity
public class Family extends DBExpandableEntity<Long> {

    private static final long serialVersionUID = 1L;
    public static final String PROP_PEOPLE = "people";
    @OneToMany(mappedBy = "family")
    private List<Person> people = new ArrayList<Person>();
    @OneToMany(mappedBy = "family")
    private List<Resource> resources = new ArrayList<Resource>();
    @OneToMany(mappedBy = "beneficiaryFamily")
    private List<Visit> visits = new ArrayList<Visit>();
    @Getter
    private String name;
    public static final String PROP_NAME = "name";
    private transient ObservableList<Person> observablePeople;
    public static final String PROP_RESOURCES = "resources";
    private transient ObservableList<Resource> observableResources;
    public static final String PROP_ADDRESS = "address";
    private transient ObservableList<Visit> observableVisits;
    public static final String PROP_VISITS = "visits";

    public void setSubscriptionBeginDate(Date newValue) {
        firePropertyChange(PROP_SUBSCRIPTION_BEGIN_DATE, subscriptionBeginDate, subscriptionBeginDate = newValue);
    }
    public static final String PROP_SUBSCRIPTION_BEGIN_DATE = "subscriptionBeginDate";
    @Getter
    @Temporal(TemporalType.DATE)
    private Date subscriptionBeginDate;

    public void setSubscriptionEndDate(Date newValue) {
        firePropertyChange(PROP_SUBSCRIPTION_END_DATE, subscriptionEndDate, subscriptionEndDate = newValue);
    }
    public static final String PROP_SUBSCRIPTION_END_DATE = "subscriptionEndDate";
    @Getter
    @Temporal(TemporalType.DATE)
    private Date subscriptionEndDate;
    @Embedded
    @Getter
    private Address address = new Address();
    @Getter
    private String telephone;
    public static final String PROP_TELEPHONE = "telephone";
    @Getter
    private String email;
    public static final String PROP_EMAIL = "email";

    public void computeDailyRemainingToLiveByPerson() {
        Float newValue = null;
        if (!(people.isEmpty() || resources.isEmpty())) {
            float total = 0;
            for (Resource res : resources) {
                total += res.getValue();
            }
            newValue = total / (people.size() * 365);
            if (newValue < 0) {
                newValue = (float) 0;
            }
        }
        firePropertyChange(PROP_DAILY_REMAINING_TO_LIVE, dailyRemainingToLiveByPerson, dailyRemainingToLiveByPerson = newValue);
    }
    @Getter
    private Float dailyRemainingToLiveByPerson;
    public static final String PROP_DAILY_REMAINING_TO_LIVE = "dailyRemainingToLiveByPerson";

    public void setEmail(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(PROP_EMAIL, email, email = newValue);
    }

    public void setTelephone(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(PROP_TELEPHONE, telephone, telephone = newValue);
    }

    public ObservableList<Person> getPeople() {
        if (observablePeople == null) {
            observablePeople = ObservableCollections.observableList(people);
            observablePeople.addObservableListListener(oneToManyObservableListListener(PROP_PEOPLE, Person.PROP_FAMILY));
        }
        return observablePeople;
    }

    public ObservableList<Resource> getResources() {
        if (observableResources == null) {
            observableResources = ObservableCollections.observableList(resources);
            observableResources.addObservableListListener(oneToManyObservableListListener(PROP_RESOURCES, Resource.PROP_FAMILY));
        }
        return observableResources;
    }

    public ObservableList<Visit> getVisits() {
        if (observableVisits == null) {
            observableVisits = ObservableCollections.observableList(visits);
            observableVisits.addObservableListListener(oneToManyObservableListListener(PROP_VISITS, Visit.PROP_BENEFICIARY_FAMILY));
        }
        return observableVisits;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    public void setName(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(PROP_NAME, name, name = newValue);
    }

    public String getDisplayName() {
        if (name != null) {
            return name;
        }
        return guessName();

    }

    public String guessName() {
        String res = null;
        Iterator<Person> it = getPeople().iterator();
        while (it.hasNext()) {
            String value = it.next().getLastName();
            if (value != null) {
                if (res == null) {
                    res = value;
                } else if (!res.equals(value)) {
                    return null;
                }
            }
        }
        return res;
    }

    public Person addNewPerson(DBReadWriteSession dbSession) {
        Person person = new Person();
        person.setLastName(getDisplayName());
        person.setFamily(this);
        dbSession.persist(person);
        return person;
    }

    public Visit addNewVisit(DBReadWriteSession dbSession) {
        Visit visit = new Visit();
        visit.setBeneficiaryFamily(this);
        dbSession.persist(visit);
        return visit;
    }

    public Resource addNewResource(DBReadWriteSession dbSession) {
        Resource resource = new Resource();
        resource.setFamily(this);
        dbSession.persist(resource);
        return resource;
    }

    public void accept(FamilyVisitor visitor) {
        if (visitor instanceof PersonVisitor) {
            PersonVisitor personVisitor = (PersonVisitor) visitor;
            for (Person p : people) {
                p.accept(personVisitor);
            }
        }
        if (visitor instanceof ResourceVisitor) {
            ResourceVisitor resourceVisitor = (ResourceVisitor) visitor;
            for (Resource r : resources) {
                r.accept(resourceVisitor);
            }
        }
        if (visitor instanceof VisitVisitor) {
            VisitVisitor visitVisitor = (VisitVisitor) visitor;
            for (Visit v : visits) {
                v.accept(visitVisitor);
            }
        }
        if (visitor instanceof AddressVisitor) {
            AddressVisitor addressVisitor = (AddressVisitor) visitor;
            address.accept(addressVisitor);
        }
        visitor.visit(this);
    }

    @Override
    public void check(ModelCheck check) {
        if (name == null) {
            check.addMessage(this, Message.MessageType.ERROR, Family.class, "Family.MissingName");
        }
        if (people.isEmpty()) {
            check.addMessage(this, Message.MessageType.ERROR, Family.class, "Family.MissingMembers");
        }
        if (subscriptionBeginDate != null && subscriptionEndDate != null && subscriptionBeginDate.after(subscriptionEndDate)) {
            check.addMessage(this, Message.MessageType.ERROR, Family.class, "Family.InconsistentSubscriptionDates");
        }
        if (!visits.isEmpty() && subscriptionBeginDate == null) {
            check.addMessage(this, Message.MessageType.ERROR, Family.class, "Family.MissingSubscriptionBeginDate");
        }
        computeDailyRemainingToLiveByPerson();
    }
}
