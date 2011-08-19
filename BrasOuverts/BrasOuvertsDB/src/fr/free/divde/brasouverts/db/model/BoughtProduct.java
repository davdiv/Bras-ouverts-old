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
public class BoughtProduct extends DBExpandableEntity<Long> {

    public void setVisit(Visit newValue) {
        manyToOnePropertyChange(PROP_VISIT, Visit.PROP_BOUGHT_PRODUCTS, visit, visit = newValue);
    }
    @Getter
    @ManyToOne
    private Visit visit;
    public static final String PROP_VISIT = "visit";

    public void setProduct(Product newValue) {
        firePropertyChange(PROP_PRODUCT, product, product = newValue);
        if (newValue != null) {
            setUnitPrice(newValue.getPrice());
        }
    }
    @Getter
    private Product product;
    public static final String PROP_PRODUCT = "product";

    public void setQuantity(int newValue) {
        firePropertyChange(PROP_PRODUCT, quantity, quantity = newValue);
    }
    @Getter
    private int quantity = 1;
    public static final String PROP_QUANTITY = "quantity";

    public void setUnitPrice(float newValue) {
        firePropertyChange(PROP_PRODUCT, unitPrice, unitPrice = newValue);
    }
    @Getter
    private float unitPrice = 0;
    public static final String PROP_UNIT_PRICE = "unitPrice";

    public float getValue() {
        return unitPrice * quantity;
    }

    @Override
    public void check(ModelCheck check) {
        // no check for now
    }
}
