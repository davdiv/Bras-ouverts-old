/*
 * Copyright (C) 2011 david
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
package fr.free.divde.brasouverts.dbui.statistics;

import fr.free.divde.brasouverts.db.DBReadOnlySession;
import fr.free.divde.brasouverts.db.model.Gender;
import fr.free.divde.brasouverts.db.statistics.DateInterval;
import fr.free.divde.brasouverts.db.statistics.StateStatistics;
import fr.free.divde.brasouverts.db.statistics.Statistics;
import java.awt.Component;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.FontHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.table.TableColumnModelExt;
import org.netbeans.api.print.PrintManager;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//fr.free.divde.brasouverts.dbui.statistics//Statistics//EN",
autostore = false)
@TopComponent.Description(preferredID = "StatisticsTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "fr.free.divde.brasouverts.dbui.statistics.StatisticsTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_StatisticsAction",
preferredID = "StatisticsTopComponent")
public final class StatisticsTopComponent extends TopComponent {

    private TableCellRenderer percentageRenderer = new DefaultTableCellRenderer() {

        @Override
        public Component getTableCellRendererComponent(JTable jtable, Object value, boolean selected, boolean focused, int row, int col) {
            Integer totalRowIndex = percentageRelation[row];
            if (totalRowIndex != null && value != null) {
                Long longValue = (Long) value;
                Long totalValue = (Long) jtable.getValueAt(totalRowIndex, col);
                if (totalValue != 0) {
                    Double percent = longValue.doubleValue() / totalValue.doubleValue();
                    value = value + " (" + NumberFormat.getPercentInstance().format(percent) + ")";
                }
            }
            return super.getTableCellRendererComponent(jtable, value, selected, focused, row, col);
        }
    };
    private String[] rowTitles = new String[]{
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.group1.title"),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.group1.registeredFamilies"),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.group1.distinctRegisteredRecipients"),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.group1.helpedRecipients"),
        null,
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.group2.title"),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.ageRangeClass", 0, 3),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.ageRangeClass", 4, 14),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.ageRangeClass", 15, 25),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.ageRangeClass", 26, 59),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.ageMinClass", 60),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.unknownClass"),
        null,
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.group3.title"),
        Gender.MALE.toString(),
        Gender.FEMALE.toString(),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.unknownClass"),
        null,
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.group4.title"),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.drtlRangeClass", 0, 2.99),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.drtlRangeClass", 3, 4.99),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.drtlRangeClass", 5, 7.99),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.drtlRangeClass", 8, 9.99),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.drtlMinClass", 10),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.unknownClass"),
        null,
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.group5.title"),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.ageRangeClass", 0, 3),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.ageRangeClass", 4, 14),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.ageRangeClass", 15, 25),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.ageRangeClass", 26, 59),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.ageMinClass", 60),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.unknownClass"),
        null,
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.group6.title"),
        Gender.MALE.toString(),
        Gender.FEMALE.toString(),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.unknownClass"),
        null,
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.group7.title"),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.drtlRangeClass", 0, 2.99),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.drtlRangeClass", 3, 4.99),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.drtlRangeClass", 5, 7.99),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.drtlRangeClass", 8, 9.99),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.drtlMinClass", 10),
        NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.unknownClass")
    };
    private Statistics[] rowStats = new Statistics[]{
        null,
        StateStatistics.queryNbRegisteredFamilies(),
        StateStatistics.queryNbRegisteredRecipients(),
        StateStatistics.queryNbHelpedRecipients(),
        null,
        null,
        StateStatistics.queryNbRegisteredRecipientsByAge(0, 4),
        StateStatistics.queryNbRegisteredRecipientsByAge(4, 15),
        StateStatistics.queryNbRegisteredRecipientsByAge(15, 26),
        StateStatistics.queryNbRegisteredRecipientsByAge(26, 60),
        StateStatistics.queryNbRegisteredRecipientsByMinAge(60),
        StateStatistics.queryNbRegisteredRecipientsByUnknownAge(),
        null,
        null,
        StateStatistics.queryNbRegisteredRecipientsByGender(Gender.MALE),
        StateStatistics.queryNbRegisteredRecipientsByGender(Gender.FEMALE),
        StateStatistics.queryNbRegisteredRecipientsByGender(null),
        null,
        null,
        StateStatistics.queryNbRegisteredRecipientsByDRTL(0, 3),
        StateStatistics.queryNbRegisteredRecipientsByDRTL(3, 5),
        StateStatistics.queryNbRegisteredRecipientsByDRTL(5, 8),
        StateStatistics.queryNbRegisteredRecipientsByDRTL(8, 10),
        StateStatistics.queryNbRegisteredRecipientsByMinDRTL(10),
        StateStatistics.queryNbRegisteredRecipientsByUnknownDRTL(),
        null,
        null,
        StateStatistics.queryNbHelpedRecipientsByAge(0, 4),
        StateStatistics.queryNbHelpedRecipientsByAge(4, 15),
        StateStatistics.queryNbHelpedRecipientsByAge(15, 26),
        StateStatistics.queryNbHelpedRecipientsByAge(26, 60),
        StateStatistics.queryNbHelpedRecipientsByMinAge(60),
        StateStatistics.queryNbHelpedRecipientsByUnknownAge(),
        null,
        null,
        StateStatistics.queryNbHelpedRecipientsByGender(Gender.MALE),
        StateStatistics.queryNbHelpedRecipientsByGender(Gender.FEMALE),
        StateStatistics.queryNbHelpedRecipientsByGender(null),
        null,
        null,
        StateStatistics.queryNbHelpedRecipientsByDRTL(0, 3),
        StateStatistics.queryNbHelpedRecipientsByDRTL(3, 5),
        StateStatistics.queryNbHelpedRecipientsByDRTL(5, 8),
        StateStatistics.queryNbHelpedRecipientsByDRTL(8, 10),
        StateStatistics.queryNbHelpedRecipientsByMinDRTL(10),
        StateStatistics.queryNbHelpedRecipientsByUnknownDRTL(),};
    private Integer percentageRelation[] = new Integer[]{
        null,
        null,
        null,
        null,
        null,
        null,
        2,
        2,
        2,
        2,
        2,
        2,
        null,
        null,
        2,
        2,
        2,
        null,
        null,
        2,
        2,
        2,
        2,
        2,
        2,
        null,
        null,
        3,
        3,
        3,
        3,
        3,
        3,
        null,
        null,
        3,
        3,
        3,
        null,
        null,
        3,
        3,
        3,
        3,
        3,
        3
    };

    public StatisticsTopComponent() {
        resultsTable = new JXTable();
        resultsTable.putClientProperty(PrintManager.PRINT_PRINTABLE, Boolean.TRUE);
        resultsTable.setColumnControlVisible(true);
        resultsTable.setSortable(false);
        Font font = resultsTable.getFont();
        Highlighter highlighter = new FontHighlighter(new HighlightPredicate() {

            @Override
            public boolean isHighlighted(Component cmpnt, ComponentAdapter ca) {
                return (rowStats[ca.row] == null);
            }
        }, font.deriveFont(Font.BOLD));
        resultsTable.addHighlighter(highlighter);
        initComponents();
        setName(NbBundle.getMessage(StatisticsTopComponent.class, "CTL_StatisticsTopComponent"));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        beginDateField = new org.jdesktop.swingx.JXDatePicker();
        endDateField = new org.jdesktop.swingx.JXDatePicker();
        statisticsButton = new javax.swing.JButton();
        beginDateLabel = new javax.swing.JLabel();
        endDateLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        javax.swing.JTable resultsTable = this.resultsTable;

        org.openide.awt.Mnemonics.setLocalizedText(statisticsButton, org.openide.util.NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.statisticsButton.text")); // NOI18N
        statisticsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statisticsButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(beginDateLabel, org.openide.util.NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.beginDateLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(endDateLabel, org.openide.util.NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.endDateLabel.text")); // NOI18N

        jScrollPane1.setViewportView(resultsTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(beginDateLabel)
                        .addGap(18, 18, 18)
                        .addComponent(beginDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(endDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(endDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statisticsButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(beginDateLabel)
                    .addComponent(beginDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endDateLabel)
                    .addComponent(endDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statisticsButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private List<DateInterval> prepareDateIntervals(Date beginDate, Date endDate) {
        DateInterval generalInterval = new DateInterval(beginDate.getTime(), endDate.getTime());
        List<DateInterval> dateIntervals = generalInterval.split();
        if (dateIntervals.size() > 1) {
            dateIntervals = new ArrayList<DateInterval>(dateIntervals);
            dateIntervals.add(generalInterval);
        }
        return dateIntervals;
    }

    private TableColumnModel prepareColumnModel(List<DateInterval> dateIntervals) {
        TableColumnModelExt tableColumnModel = new DefaultTableColumnModelExt();
        int i = 0;
        TableColumnExt column = new TableColumnExt(i);
        column.setTitle(NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.descriptionColumn.title"));
        tableColumnModel.addColumn(column);
        for (DateInterval interval : dateIntervals) {
            i++;
            column = new TableColumnExt(i);
            String title = interval.toString();
            title = Character.toUpperCase(title.charAt(0)) + title.substring(1);
            column.setTitle(title);
            column.setCellRenderer(percentageRenderer);
            tableColumnModel.addColumn(column);
        }

        return tableColumnModel;
    }

    private DefaultTableModel prepareTableModel(List<DateInterval> dateIntervals) {
        DefaultTableModel tableModel = new DefaultTableModel(rowStats.length, dateIntervals.size() + 1);
        DBReadOnlySession dbSession = new DBReadOnlySession();
        int row = 0;
        for (String rowTitle : rowTitles) {
            tableModel.setValueAt(rowTitle, row, 0);
            row++;
        }
        row = 0;
        for (Statistics stats : rowStats) {
            if (stats != null) {
                stats.setDbSession(dbSession);
                int col = 1;
                for (DateInterval interval : dateIntervals) {
                    tableModel.setValueAt(stats.getValue(interval), row, col);
                    col++;
                }
            }
            row++;
        }
        dbSession.close();
        return tableModel;
    }

private void statisticsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statisticsButtonActionPerformed
    Date beginDate = beginDateField.getDate();
    Date endDate = endDateField.getDate();
    if (beginDate == null || endDate == null || endDate.before(beginDate)) {
        JOptionPane.showMessageDialog(this, NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.checkDateFieldsMessage"), NbBundle.getMessage(StatisticsTopComponent.class, "StatisticsTopComponent.checkDateFieldsTitle"), JOptionPane.ERROR_MESSAGE);
        return;
    }
    List<DateInterval> dateIntervals = prepareDateIntervals(beginDate, endDate);
    TableColumnModel columnModel = prepareColumnModel(dateIntervals);
    DefaultTableModel tableModel = prepareTableModel(dateIntervals);
    resultsTable.setModel(tableModel);
    resultsTable.setColumnModel(columnModel);

}//GEN-LAST:event_statisticsButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXDatePicker beginDateField;
    private javax.swing.JLabel beginDateLabel;
    private org.jdesktop.swingx.JXDatePicker endDateField;
    private javax.swing.JLabel endDateLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton statisticsButton;
    // End of variables declaration//GEN-END:variables
    private JXTable resultsTable;

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
