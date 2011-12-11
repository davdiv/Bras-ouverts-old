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

import java.text.ParseException;
import javax.swing.text.InternationalFormatter;
import org.jdesktop.swingx.JXTextField;

public abstract class FormattedCellEditor<T> extends CellRendererEditor<JXTextField, T> {

    private InternationalFormatter[] formatters;

    public FormattedCellEditor(InternationalFormatter... formatters) {
        this.formatters = formatters;
        component = new JXTextField();
        component.addActionListener(initActionListener());
    }

    @Override
    protected void prepareComponent() {
        try {
            component.setText(formatters[0].valueToString(value));
            component.selectAll();
        } catch (ParseException ex) {
            throw new AssertionError(ex);
        }

    }

    @Override
    protected void updateValue() {
        String text = component.getText();
        for (InternationalFormatter formatter : formatters) {
            try {
                value = (T) formatter.stringToValue(text);
                return;
            } catch (ParseException ex) {
            }
        }
    }
}
