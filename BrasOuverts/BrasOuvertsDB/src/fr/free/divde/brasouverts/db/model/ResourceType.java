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
import fr.free.divde.brasouverts.db.check.ModelCheck;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.TypedQuery;
import lombok.Getter;
import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.ObjectTypeConverter;

@Entity
public class ResourceType extends DBEntity<Long> {

    public static List<ResourceType> getResourceTypes(DBReadOnlySession dbSession) {
        TypedQuery<ResourceType> query = dbSession.createQuery("SELECT r FROM ResourceType r", ResourceType.class);
        return query.getResultList();
    }
    private static final long serialVersionUID = 1L;

    @Override
    public void check(ModelCheck check) {
    }

    public static enum Type {

        REVENUE(1),
        EXPENDITURE(-1);

        private Type(float coefficient) {
            this.coefficient = coefficient;
        }
        private float coefficient;

        public float getCoefficient() {
            return coefficient;
        }
    }
    @ObjectTypeConverter(name = "resourceType", objectType = Type.class, dataType = Integer.class, conversionValues = {
        @ConversionValue(objectValue = "REVENUE", dataValue = "1"),
        @ConversionValue(objectValue = "EXPENDITURE", dataValue = "-1")})
    @Convert("resourceType")
    @Basic(optional = false)
    @Getter
    private Type type;
    public static final String PROP_TYPE = "type";
    @Getter
    private String name;
    public static final String PROP_NAME = "name";

    public void setName(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(PROP_NAME, name, name = newValue);
    }

    public void setType(Type newValue) {
        firePropertyChange(PROP_TYPE, type, type = newValue);
    }

    public float getCoefficient() {
        return (type == null ? 0 : type.getCoefficient());
    }

    @Override
    public String toString() {
        return getName();
    }
}
