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

import fr.free.divde.brasouverts.db.check.Message.MessageType;
import fr.free.divde.brasouverts.db.check.ModelCheck;
import fr.free.divde.brasouverts.db.visitors.ResourceVisitor;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
public class Resource extends DBExpandableEntity<Long> {

    public void setPeriodicity(Periodicity newValue) {
        firePropertyChange(PROP_PERIODICITY, periodicity, periodicity = newValue);
    }
    public static String PROP_PERIODICITY = "periodicity";
    @Getter
    private Periodicity periodicity;
    public static String PROP_FAMILY = "family";
    @ManyToOne
    @Getter
    private Family family;
    public static String PROP_RESOURCETYPE = "resourceType";
    @ManyToOne
    @Getter
    private ResourceType resourceType;
    public static String PROP_AMOUNT = "amount";
    @Getter
    private float amount = 0;

    public void setResourceType(ResourceType newValue) {
        firePropertyChange(PROP_RESOURCETYPE, resourceType, resourceType = newValue);
    }

    public void setAmount(float newValue) {
        firePropertyChange(PROP_AMOUNT, amount, amount = newValue);
    }

    public void setFamily(Family newValue) {
        manyToOnePropertyChange(PROP_FAMILY, Family.PROP_RESOURCES, family, family = newValue);
    }

    public float getValue() {
        return (resourceType == null || periodicity == null ? 0 : amount * resourceType.getCoefficient() * periodicity.getCoefficient());
    }

    public void accept(ResourceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void check(ModelCheck check) {
        if (resourceType == null) {
            check.addMessage(this, MessageType.ERROR, Resource.class, "Resource.MissingResourceType");
        }
        if (periodicity == null) {
            check.addMessage(this, MessageType.ERROR, Resource.class, "Resource.MissingPeriodicity");
        }
        if (amount < 0) {
            check.addMessage(this, MessageType.ERROR, Resource.class, "Resource.NegativeAmount");
        }
    }
}
