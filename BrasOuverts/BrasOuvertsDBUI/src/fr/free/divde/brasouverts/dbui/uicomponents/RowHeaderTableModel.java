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

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class RowHeaderTableModel implements TableModel {

    private TableModel realModel;

    public RowHeaderTableModel(TableModel model) {
        realModel = model;
    }

    @Override
    public int getRowCount() {
        return realModel.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int i) {
        return "";
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return Integer.class;
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        return row + 1;
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {
        realModel.addTableModelListener(tl);
    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {
        realModel.removeTableModelListener(tl);
    }
}
