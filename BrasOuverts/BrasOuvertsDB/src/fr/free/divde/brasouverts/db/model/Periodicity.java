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

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.openide.util.NbBundle;

public enum Periodicity {

    DAILY("Periodicity.Daily", 365), MONTHLY("Periodicity.Monthly", 12), YEARLY("Periodicity.Yearly", 1);
    private String bundleId;
    @Getter
    private float coefficient;

    private Periodicity(String bundleId, float coefficient) {
        this.bundleId = bundleId;
        this.coefficient = coefficient;
    }

    @Override
    public String toString() {
        return NbBundle.getMessage(Periodicity.class, bundleId);
    }

    public static List<Periodicity> getValues() {
        return Arrays.asList(null, DAILY, MONTHLY, YEARLY);
    }
}
