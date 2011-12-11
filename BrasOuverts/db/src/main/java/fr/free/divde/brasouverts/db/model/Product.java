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
import lombok.Getter;

@Entity
public class Product extends DBExpandableEntity<Long> {

    public void setName(String newValue) {
        newValue = filterEmptyValue(newValue);
        firePropertyChange(PROP_NAME, name, name = newValue);
    }
    @Getter
    private String name;
    public static final String PROP_NAME = "name";

    public void setPrice(float newValue) {
        firePropertyChange(PROP_PRICE, price, price = newValue);
    }
    @Getter
    private float price;
    public static final String PROP_PRICE = "price";

    @Override
    public void check(ModelCheck check) {
    }
}
