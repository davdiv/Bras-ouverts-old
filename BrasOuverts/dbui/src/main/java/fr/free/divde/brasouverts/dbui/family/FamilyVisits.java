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
package fr.free.divde.brasouverts.dbui.family;

import fr.free.divde.brasouverts.db.BrasOuvertsDB;
import fr.free.divde.brasouverts.db.DBReadWriteSession;
import fr.free.divde.brasouverts.db.model.Family;
import fr.free.divde.brasouverts.db.model.Visit;
import fr.free.divde.brasouverts.dbui.VisitsCalendar;
import fr.free.divde.brasouverts.dbui.VisitsList;
import fr.free.divde.brasouverts.dbui.uicomponents.BindableComponent;
import fr.free.divde.brasouverts.dbui.uicomponents.ExpandableScrollPane;
import fr.free.divde.brasouverts.dbui.uicomponents.JXXTable;
import fr.free.divde.brasouverts.dbui.uicomponents.ListAction;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import lombok.Getter;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXTaskPane;
import org.openide.util.NbBundle;

public class FamilyVisits extends JXTaskPane implements BindableComponent {

    public static final String PROP_DBSESSION = "dbSession";
    @Getter
    protected DBReadWriteSession dbSession;
    public static final String PROP_FAMILY = "family";
    @Getter
    protected Family family;

    public void setDbSession(DBReadWriteSession newValue) {
        firePropertyChange(PROP_DBSESSION, dbSession, dbSession = newValue);
    }

    public void setFamily(Family newValue) {
        firePropertyChange(PROP_FAMILY, family, family = newValue);
    }

    /** Creates new form Visits */
    public FamilyVisits() {
        setTitle(NbBundle.getMessage(FamilyVisits.class, "FamilyVisits.title"));
        initComponents();
    }

    private void initComponents() {

        add(new AbstractAction(NbBundle.getMessage(FamilyVisits.class, "FamilyVisits.addVisit")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = calendar.getSelectionDate();
                if (date != null && !calendar.isFlaggedDate(date)) {
                    addVisitDate(date);
                } else {
                    JOptionPane.showMessageDialog(FamilyVisits.this, NbBundle.getMessage(FamilyVisits.class, "FamilyVisits.pleaseSelectDateMessage"), NbBundle.getMessage(FamilyVisits.class, "FamilyVisits.pleaseSelectDateTitle"), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        add(new AbstractAction(NbBundle.getMessage(FamilyVisits.class, "FamilyVisits.removeSelectedVisits")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                visitsTable.executeOnSelectedItems("JXXTable.remove", new ListAction<Visit>() {

                    @Override
                    public void performAction(int index, Visit visit) {
                        dbSession.remove(visit);
                        visit.setBeneficiaryFamily(null);
                    }
                });
            }
        });
        calendar.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isShiftDown()) {
                    Date date = calendar.getDayAtLocation(e.getX(), e.getY());
                    if (date != null) {
                        if (!calendar.isFlaggedDate(date)) {
                            addVisitDate(date);
                        }
                    }
                }
            }
        });
        calendar.setBackground(getBackground());
        add(calendar);
        add(new ExpandableScrollPane(visitsTable));

    }
    private VisitsCalendar calendar = new VisitsCalendar();
    private VisitsList visitsTable = new VisitsList();
    private BindingGroup bindingGroup;

    public Visit addVisitDate(Date date) {
        int index = family.getVisits().size();
        Visit visit = family.addNewVisit(dbSession);
        visit.setVisitDate(date);
        assert index + 1 == family.getVisits().size();
        calendar.setSelectedIndex(index);
        return visit;
    }

    @Override
    public void bind() {
        if (bindingGroup == null) {
            bindingGroup = new BindingGroup();
            Binding binding;
            BeanProperty visits = BeanProperty.create(BeanProperty.create(PROP_FAMILY), Family.PROP_VISITS);
            binding = Bindings.createAutoBinding(UpdateStrategy.READ, this, visits, calendar, BeanProperty.create(VisitsCalendar.PROP_VISITS));
            bindingGroup.addBinding(binding);
            binding = Bindings.createAutoBinding(UpdateStrategy.READ, this, visits, visitsTable, BeanProperty.create(JXXTable.PROP_ITEMS));
            bindingGroup.addBinding(binding);
            binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, calendar, BeanProperty.create(VisitsCalendar.PROP_SELECTED_INDEX), visitsTable, BeanProperty.create(JXXTable.PROP_SELECTED_INDEX));
            bindingGroup.addBinding(binding);
        }
        bindingGroup.bind();
        visitsTable.bind();
        calendar.bind();
    }

    @Override
    public void unbind() {
        if (bindingGroup != null) {
            visitsTable.unbind();
            calendar.unbind();
            bindingGroup.unbind();
        }
    }
}
