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
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import lombok.Getter;
import lombok.Setter;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.table.TableColumnModelExt;
import org.openide.util.NbBundle;

public abstract class JXXTable<T> extends JTableWithRowHeader implements BindableComponent {
    
    public static final String PROP_ITEMS = "items";
    @Getter
    protected List<T> items = Collections.EMPTY_LIST;
    private JTableBinding jTableBinding;
    private TableColumnModel swapColumnModel;
    private TableColumnModelExt normalColumnModel;
    public static final String PROP_SELECTED_INDEX = "selectedIndex";
    @Getter
    private int selectedIndex = -1;
    
    public void setSelectedIndex(int newValue) {
        if (newValue >= 0) {
            int row = convertRowIndexToView(newValue);
            if (row >= 0) {
                getSelectionModel().setSelectionInterval(row, row);
                scrollRowToVisible(row);
            }
        }
        firePropertyChange(PROP_SELECTED_INDEX, selectedIndex, selectedIndex = newValue);
    }
    
    public JXXTable() {
    }
    
    protected void initComponent() {
        assert jTableBinding == null;
        setAutoResizeMode(AUTO_RESIZE_OFF);
        setRowHeight(26);
        setRowMargin(1);
        setRowSelectionAllowed(true);
        setTerminateEditOnFocusLost(true);
        setAutoCreateColumnsFromModel(false);
        setColumnControlVisible(true);
        
        addPropertyChangeListener("tableCellEditor", new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Component component = getEditorComponent();
                if (component != null) {
                    component.requestFocusInWindow();
                }
            }
        });
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int rowIndex = -1;
                if (getSelectedRowCount() == 1) {
                    rowIndex = convertRowIndexToModel(getSelectedRow());
                }
                setSelectedIndex(rowIndex);
            }
        });
        
        Column[] columns = createColumns();
        processColumns(columns);
    }
    
    private void processColumns(Column[] columns) {
        normalColumnModel = new DefaultTableColumnModelExt();
        
        BeanProperty itemsProperty = BeanProperty.create("items");
        jTableBinding = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ_WRITE, this, itemsProperty, this);
        
        for (int i = 0; i < columns.length; i++) {
            Column column = columns[i];
            JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(column.getProperty());
            columnBinding.setColumnName(column.getId());
            columnBinding.setColumnClass(column.getType());
            columnBinding.setConverter(column.getConverter());
            columnBinding.setEditable(column.isEditable());
            column.setModelIndex(i);
            normalColumnModel.addColumn(column);
        }
        
        jTableBinding.bind();
        setColumnModel(normalColumnModel);
    }
    
    protected abstract Column[] createColumns();
    
    public void setItems(List<T> newValue) {
        if (newValue == null) {
            return;
        }
        firePropertyChange(PROP_ITEMS, items, items = newValue);
    }
    
    @Override
    public void unbind() {
        if (jTableBinding != null && jTableBinding.isBound()) {
            // jTableBinding is such that if we run unbind, there is an exception unless the column model is changed
            if (swapColumnModel == null) {
                swapColumnModel = new DefaultTableColumnModel();
            }
            setColumnModel(swapColumnModel);
            jTableBinding.unbind();
        }
    }
    
    @Override
    public void bind() {
        if (jTableBinding == null) {
            initComponent();
        } else if (!jTableBinding.isBound()) {
            jTableBinding.bind();
            setColumnModel(normalColumnModel);
        }
    }
    
    public void executeOnSelectedItems(String confirmationKeyPrefix, ListAction<T> action) {
        int[] selectedRows = getSelectedRows();
        if (selectedRows.length > 0) {
            int confirmed = confirmationKeyPrefix != null ? JOptionPane.showConfirmDialog(this, NbBundle.getMessage(JXXTable.class, confirmationKeyPrefix + "ConfirmationMessage", selectedRows.length), NbBundle.getMessage(JXXTable.class, confirmationKeyPrefix + "ConfirmationTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) : JOptionPane.YES_OPTION;
            if (confirmed == JOptionPane.YES_OPTION) {
                // convert row indexes to model indexes:
                for (int i = 0; i < selectedRows.length; i++) {
                    selectedRows[i] = convertRowIndexToModel(selectedRows[i]);
                }
                // sort model indexes:
                Arrays.sort(selectedRows);
                // remove items from the model:
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int index = selectedRows[i];
                    action.performAction(index, items.get(index));
                }
            }
        }
    }
    
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    /**
     * Set in editable mode the first null cell in the given row.
     * @param rowIndex 
     */
    public void editRow(int rowIndex) {
        int columns = getColumnCount();
        for (int i = 0; i < columns; i++) {
            if (getValueAt(rowIndex, i) == null) {
                editCellAt(rowIndex, i);
                return;
            }
        }
    }
    
    protected class Column extends TableColumnExt {
        
        @Getter
        @Setter
        private Class type;
        @Getter
        @Setter
        private Property property;
        @Getter
        @Setter
        private Converter converter;
        @Getter
        private String id;
        
        public void setId(String id) {
            this.id = id;
            setIdentifier(id);
        }
        
        public Column(String id, String property, Class type, TableCellEditor editor, int preferredWidth) {
            setTitle(NbBundle.getMessage(JXXTable.this.getClass(), id));
            setCellEditor(editor);
            if (editor == null) {
                setEditable(false);
            }
            this.property = BeanProperty.create(property);
            this.type = type;
            this.id = id;
            setPreferredWidth(preferredWidth);
        }
    }
}
