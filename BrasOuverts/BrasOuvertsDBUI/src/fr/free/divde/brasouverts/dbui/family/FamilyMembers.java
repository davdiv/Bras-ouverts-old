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
import fr.free.divde.brasouverts.db.model.Person;
import fr.free.divde.brasouverts.dbui.uicomponents.ExpandableScrollPane;
import fr.free.divde.brasouverts.dbui.PeopleList;
import fr.free.divde.brasouverts.dbui.uicomponents.BindableComponent;
import fr.free.divde.brasouverts.dbui.uicomponents.JXXTable;
import fr.free.divde.brasouverts.dbui.uicomponents.ListAction;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import lombok.Getter;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXTaskPane;
import org.openide.util.NbBundle;

public class FamilyMembers extends JXTaskPane implements BindableComponent {

    protected PeopleList membersTable;
    public static final String PROP_DBSESSION = "dbSession";
    @Getter
    protected DBReadWriteSession dbSession;
    public static final String PROP_FAMILY = "family";
    @Getter
    protected Family family;
    private BindingGroup bindingGroup;

    public void setDbSession(DBReadWriteSession newValue) {
        firePropertyChange(PROP_DBSESSION, dbSession, dbSession = newValue);
    }

    public void setFamily(Family newValue) {
        firePropertyChange(PROP_FAMILY, family, family = newValue);
    }

    public FamilyMembers() {
        initComponent();
    }

    private void initComponent() {

        setTitle(NbBundle.getMessage(FamilyMembers.class, "FamilyMembers.title"));

        AbstractAction addMember = new AbstractAction(NbBundle.getMessage(FamilyMembers.class, "FamilyMembers.addMember")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                addMember();
            }
        };
        add(addMember);
        AbstractAction removeSelectedMembers = new AbstractAction(NbBundle.getMessage(FamilyMembers.class, "FamilyMembers.removeSelectedMembers")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedMembers();
            }
        };
        add(removeSelectedMembers);
        membersTable = new PeopleList();
        add(new ExpandableScrollPane(membersTable));

        bindingGroup = new BindingGroup();
        Binding binding;
        binding = Bindings.createAutoBinding(UpdateStrategy.READ, this, BeanProperty.create(BeanProperty.create(PROP_DBSESSION), "countries"), membersTable, BeanProperty.create(PeopleList.PROP_COUNTRIES));
        bindingGroup.addBinding(binding);
        binding = Bindings.createAutoBinding(UpdateStrategy.READ, this, BeanProperty.create(BeanProperty.create(PROP_FAMILY), Family.PROP_PEOPLE), membersTable, BeanProperty.create(JXXTable.PROP_ITEMS));
        bindingGroup.addBinding(binding);
    }

    public void addMember() {
        int newIndex = family.getPeople().size();
        family.addNewPerson(dbSession);
        assert family.getPeople().size() == newIndex + 1;

        // the new person is added at the end of the model
        // but, maybe, rows are sorted in another way
        int row = membersTable.convertRowIndexToView(newIndex);
        // automatically select the new row:
        membersTable.getSelectionModel().setSelectionInterval(row, row);
        // edit the first cell in the row immediately:
        membersTable.editRow(row);
    }

    public void removeSelectedMembers() {
        membersTable.executeOnSelectedItems("JXXTable.remove", new ListAction<Person>() {

            @Override
            public void performAction(int index, Person person) {
                dbSession.remove(person);
                person.setFamily(null);
            }
        });
    }

    @Override
    public void bind() {
        bindingGroup.bind();
        membersTable.bind();
    }

    @Override
    public void unbind() {
        membersTable.unbind();
        bindingGroup.unbind();
    }
}
