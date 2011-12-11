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

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.JXTable;

public class JTableWithRowHeader extends JXTable {

    private JTable rowHeader = new JTable() {

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return super.getPreferredSize();
        }
    };

    public JTableWithRowHeader() {
        rowHeader.setAutoCreateColumnsFromModel(false);
        rowHeader.setModel(new RowHeaderTableModel(getModel()));
        rowHeader.setRowHeight(getRowHeight());
        rowHeader.setRowMargin(getRowMargin());
        rowHeader.setSelectionModel(getSelectionModel());
        TableColumnModel model = new DefaultTableColumnModel();
        TableColumn column = new TableColumn(0);
        column.setPreferredWidth(32);
        column.setCellRenderer(new RowHeaderRenderer());
        model.addColumn(column);
        rowHeader.setColumnModel(model);
    }

    @Override
    protected void configureEnclosingScrollPane() {
        super.configureEnclosingScrollPane();
        Container parent = getParent();
        if (parent instanceof JViewport) {
            parent = parent.getParent();
            if (parent instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) parent;
                scrollPane.setViewportBorder(null);
                scrollPane.setRowHeaderView(rowHeader);
                scrollPane.setCorner(ScrollPaneConstants.UPPER_LEADING_CORNER, rowHeader.getTableHeader());
            }
        }
    }

    public JTable getRowHeader() {
        return rowHeader;
    }

    @Override
    public void setSelectionModel(ListSelectionModel lsm) {
        super.setSelectionModel(lsm);
        if (rowHeader != null) {
            // note: rowHeader can be null as setModel is called in the constructor of JTable
            rowHeader.setSelectionModel(lsm);
        }
    }
    
    @Override
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        if (rowHeader != null) {
            // note: rowHeader can be null as setModel is called in the constructor of JTable
            rowHeader.setRowHeight(rowHeight);
        }
    }

    @Override
    public void setRowHeight(int row, int height) {
        super.setRowHeight(row, height);
        // note: rowHeader can be null as setModel is called in the constructor of JTable
        rowHeader.setRowHeight(row, height);
    }

    @Override
    public void setRowMargin(int i) {
        super.setRowMargin(i);
        if (rowHeader != null) {
            // note: rowHeader can be null as setModel is called in the constructor of JTable
            rowHeader.setRowMargin(i);
        }
    }

    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
        if (rowHeader != null) {
            // note: rowHeader can be null as setModel is called in the constructor of JTable
            rowHeader.setModel(new RowHeaderTableModel(dataModel));
        }
    }
}
