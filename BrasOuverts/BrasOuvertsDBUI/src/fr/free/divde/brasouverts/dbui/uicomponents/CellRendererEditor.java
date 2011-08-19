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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public abstract class CellRendererEditor<T extends Component, V> extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

    protected JTable table;
    protected V value;
    protected boolean isSelected;
    protected boolean hasFocus;
    protected int row;
    protected int column;
    protected T component;
    protected EventObject cellEditingEvent;

    @Override
    public Object getCellEditorValue() {
        updateValue();
        return value;
    }

    protected abstract void prepareComponent();

    protected abstract void updateValue();

    protected ActionListener initActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stopCellEditing();
            }
        };
    }

    private Component getComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.value = (V) value;
        this.isSelected = isSelected;
        this.row = row;
        this.column = column;
        prepareComponent();
        return component;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return getComponent(table, value, isSelected, row, column);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.hasFocus = hasFocus;
        return getComponent(table, value, isSelected, row, column);
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        cellEditingEvent = e;
        return super.isCellEditable(e);
    }

    public T getComponent() {
        return component;
    }
}
