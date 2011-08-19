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
package fr.free.divde.brasouverts.dbui.uicomponents;

import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;

public class CurrencyEditor extends FormattedCellEditor<Float> {

    private static NumberFormatter initCurrencyFormatter() {
        NumberFormatter res = new NumberFormatter(NumberFormat.getCurrencyInstance());
        res.setValueClass(Float.class);
        return res;
    }

    private static NumberFormatter initNumberFormatter() {
        NumberFormatter res = new NumberFormatter();
        res.setValueClass(Float.class);
        return res;
    }

    public CurrencyEditor() {
        super(initCurrencyFormatter(), initNumberFormatter());
    }
}
