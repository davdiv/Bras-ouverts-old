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
package fr.free.divde.brasouverts.dbui;

import fr.free.divde.brasouverts.db.model.Periodicity;
import fr.free.divde.brasouverts.dbui.uicomponents.JXXTable;
import fr.free.divde.brasouverts.dbui.uicomponents.ComboBoxCellEditor;
import fr.free.divde.brasouverts.dbui.uicomponents.StringCellEditor;
import fr.free.divde.brasouverts.db.model.Resource;
import fr.free.divde.brasouverts.db.model.ResourceType;
import fr.free.divde.brasouverts.dbui.uicomponents.CurrencyRenderer;
import fr.free.divde.brasouverts.dbui.uicomponents.CurrencyEditor;
import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;

public class ResourcesList extends JXXTable<Resource> {

    @Getter
    protected List<ResourceType> resourceTypes = Arrays.asList((ResourceType) null);
    public static String PROP_RESOURCETYPES = "resourceTypes";
    private static Highlighter revenue = new ColorHighlighter(new HighlightPredicate() {

        @Override
        public boolean isHighlighted(Component cmpnt, ComponentAdapter ca) {
            Object value = ca.getValue(0);
            if (value instanceof ResourceType) {
                return ((ResourceType) value).getCoefficient() > 0;
            }
            return false;
        }
    }, new Color(200, 255, 200), Color.BLACK, new Color(0, 180, 0), Color.WHITE);
    private static Highlighter expenditure = new ColorHighlighter(new HighlightPredicate() {

        @Override
        public boolean isHighlighted(Component cmpnt, ComponentAdapter ca) {
            Object value = ca.getValue(0);
            if (value instanceof ResourceType) {
                return ((ResourceType) value).getCoefficient() < 0;
            }
            return false;
        }
    }, new Color(255, 200, 200), Color.BLACK, Color.RED, Color.WHITE);

    public void setResourceTypes(List<ResourceType> newValue) {
        firePropertyChange(PROP_RESOURCETYPES, resourceTypes, resourceTypes = newValue);
    }

    @Override
    protected void initComponent() {
        super.initComponent();
        addHighlighter(revenue);
        addHighlighter(expenditure);
    }

    @Override
    protected Column[] createColumns() {

        // TODO: bind resourceTypes instead of only accessing it
        ComboBoxCellEditor resourceTypesCellEditor = new ComboBoxCellEditor(resourceTypes);
        JXComboBox comboBox = resourceTypesCellEditor.getComponent();
        comboBox.addHighlighter(revenue);
        comboBox.addHighlighter(expenditure);
        Column amount = new Column("ResourcesList.Amount", Resource.PROP_AMOUNT, Float.class, new CurrencyEditor(), 150);
        amount.setCellRenderer(new CurrencyRenderer());
        Column[] res = new JXXTable.Column[]{
            new Column("ResourcesList.ResourceType", Resource.PROP_RESOURCETYPE, ResourceType.class, resourceTypesCellEditor, 150),
            new Column("ResourcesList.Periodicity", Resource.PROP_PERIODICITY, Periodicity.class, new ComboBoxCellEditor(Periodicity.getValues()), 150),
            amount,
            new Column("ResourcesList.Comments", Resource.PROP_COMMENT, String.class, new StringCellEditor(), 150)
        };
        return res;
    }

}
