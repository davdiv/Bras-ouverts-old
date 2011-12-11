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
import fr.free.divde.brasouverts.db.visitors.VisitVisitor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;

@Entity
public class Visit extends DBExpandableEntity<Long> {

    public void setPerson(Person newValue) {
        firePropertyChange(PROP_PERSON, newValue, person = newValue);
    }
    @Getter
    private Person person;
    public static final String PROP_PERSON = "person";

    public void setBeneficiaryFamily(Family newValue) {
        manyToOnePropertyChange(PROP_BENEFICIARY_FAMILY, Family.PROP_VISITS, beneficiaryFamily, beneficiaryFamily = newValue);
    }
    @Getter
    @ManyToOne
    private Family beneficiaryFamily;
    public static final String PROP_BENEFICIARY_FAMILY = "beneficiaryFamily";

    public void setVisitDate(Date newValue) {
        firePropertyChange(PROP_VISIT_DATE, visitDate, visitDate = newValue);
    }
    @Getter
    @Temporal(TemporalType.DATE)
    private Date visitDate;
    public static final String PROP_VISIT_DATE = "visitDate";

    public void setNbPackets(Integer newValue) {
        firePropertyChange(PROP_ID, nbPackets, nbPackets = newValue);
    }
    public static final String PROP_NBPACKETS = "nbPackets";
    @Getter
    private Integer nbPackets = 1;

    public ObservableList<BoughtProduct> getBoughtProducts() {
        if (observableBoughtProducts == null) {
            observableBoughtProducts = ObservableCollections.observableList(boughtProducts);
            observableBoughtProducts.addObservableListListener(oneToManyObservableListListener(PROP_BOUGHT_PRODUCTS, BoughtProduct.PROP_VISIT));
        }
        return observableBoughtProducts;
    }
    @OneToMany(mappedBy = "visit")
    private List<BoughtProduct> boughtProducts = new ArrayList<BoughtProduct>();
    private transient ObservableList<BoughtProduct> observableBoughtProducts;
    public static final String PROP_BOUGHT_PRODUCTS = "boughtProducts";

    public void setPreviousDebt(Float newValue) {
        firePropertyChange(PROP_PREVIOUS_DEBT, newValue, previousDebt = newValue);
    }
    @Getter
    private Float previousDebt;
    public static final String PROP_PREVIOUS_DEBT = "previousDebt";

    public void setProductsTotalPrice(Float newValue) {
        firePropertyChange(PROP_PRODUCTS_TOTAL_PRICE, productsTotalPrice, productsTotalPrice = newValue);
    }
    @Getter
    private Float productsTotalPrice;
    public static final String PROP_PRODUCTS_TOTAL_PRICE = "productsTotalPrice";

    public void setDiscount(Float newValue) {
        firePropertyChange(PROP_DISCOUNT, discount, discount = newValue);
    }
    @Getter
    private Float discount;
    public static final String PROP_DISCOUNT = "discount";

    public void setAmountPaid(Float newValue) {
        firePropertyChange(PROP_AMOUNT_PAID, amountPaid, amountPaid = newValue);
    }
    @Getter
    private Float amountPaid;
    public static final String PROP_AMOUNT_PAID = "amountPaid";

    public void setRemainingDebt(Float newValue) {
        firePropertyChange(PROP_REMAINING_DEBT, remainingDebt, remainingDebt = newValue);
    }
    @Getter
    private Float remainingDebt;
    public static final String PROP_REMAINING_DEBT = "remainingDebt";

    public void accept(VisitVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void check(ModelCheck check) {
        if (beneficiaryFamily == null) {
            check.addMessage(this, MessageType.ERROR, Visit.class, "Visit.MissingBeneficiaryFamily");
        }
        if (visitDate == null) {
            check.addMessage(this, MessageType.ERROR, Visit.class, "Visit.MissingVisitDate");
        } else {
            if (visitDate.getTime() > System.currentTimeMillis()) {
                check.addMessage(this, MessageType.ERROR, Visit.class, "Visit.VisitDateInFuture");
            }
        }
        if (beneficiaryFamily != null && visitDate != null) {
            Date beginDate = beneficiaryFamily.getSubscriptionBeginDate();
            if (beginDate != null && visitDate.before(beginDate)) {
                check.addMessage(this, MessageType.ERROR, Visit.class, "Visit.VisitDateBeforeSubscriptionBegin");
            }
            Date endDate = beneficiaryFamily.getSubscriptionEndDate();
            if (endDate != null && visitDate.after(endDate)) {
                check.addMessage(this, MessageType.ERROR, Visit.class, "Visit.VisitDateAfterSubscriptionEnd");
            }
        }

    }
}
