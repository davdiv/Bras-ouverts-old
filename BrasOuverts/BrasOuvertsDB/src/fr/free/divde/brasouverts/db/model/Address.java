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
import fr.free.divde.brasouverts.db.visitors.AddressVisitor;
import javax.persistence.Embeddable;
import lombok.Getter;

@Embeddable
public class Address extends DBObject {
    
    public static final String CHEZ_PROP = "chez";
    @Getter
    private String chez;
    public static final String ADDRESS_PROP = "address";
    @Getter
    private String address;
    public static final String NUMBER_PROP = "number";
    @Getter
    private String number;
    public static final String STREET_PROP = "street";
    @Getter
    private String street;
    public static final String CITY_PROP = "city";
    @Getter
    private String city;
    public static final String POSTAL_CODE_PROP = "postalCode";
    @Getter
    private String postalCode;
    public static final String COUNTRY_PROP = "country";
    @Getter
    private Country country;
    
    public void setAddress(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(ADDRESS_PROP, address, address = newValue);
    }
    
    public void setChez(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(CHEZ_PROP, chez, chez = newValue);
    }
    
    public void setCity(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(CITY_PROP, city, city = newValue);
    }
    
    public void setCountry(Country newValue) {
        firePropertyChange(COUNTRY_PROP, country, country = newValue);
    }
    
    public void setNumber(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(NUMBER_PROP, number, number = newValue);
    }
    
    public void setPostalCode(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(POSTAL_CODE_PROP, postalCode, postalCode = newValue);
    }
    
    public void setStreet(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(STREET_PROP, street, street = newValue);
    }
    
    @Override
    public void check(ModelCheck check) {
        // no check for now
    }
    
    public void accept(AddressVisitor visitor) {
        visitor.visit(this);
    }
}
