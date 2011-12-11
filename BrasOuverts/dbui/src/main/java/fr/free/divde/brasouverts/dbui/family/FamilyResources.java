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

import lombok.Getter;
import fr.free.divde.brasouverts.db.DBReadWriteSession;
import fr.free.divde.brasouverts.db.model.Family;
import fr.free.divde.brasouverts.db.model.Resource;
import fr.free.divde.brasouverts.dbui.uicomponents.ExpandableScrollPane;
import fr.free.divde.brasouverts.dbui.ResourcesList;
import fr.free.divde.brasouverts.dbui.uicomponents.BindableComponent;
import fr.free.divde.brasouverts.dbui.uicomponents.JXXTable;
import fr.free.divde.brasouverts.dbui.uicomponents.ListAction;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXTaskPane;
import org.openide.util.NbBundle;

public class FamilyResources extends JXTaskPane implements BindableComponent {

    private FamilyResourcesInfo resourcesInfo;
    protected ResourcesList resourcesTable;
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

    public FamilyResources() {
        initComponent();
    }

    private void initComponent() {
        setTitle(NbBundle.getMessage(FamilyMembers.class, "FamilyResources.title"));
        AbstractAction addMember = new AbstractAction(NbBundle.getMessage(FamilyMembers.class, "FamilyResources.addResource")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                addResource();
            }
        };
        add(addMember);
        AbstractAction removeSelectedMembers = new AbstractAction(NbBundle.getMessage(FamilyMembers.class, "FamilyResources.removeSelectedResources")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedResources();
            }
        };
        add(removeSelectedMembers);
        resourcesTable = new ResourcesList();
        add(new ExpandableScrollPane(resourcesTable));
        resourcesInfo = new FamilyResourcesInfo();
        add(resourcesInfo);
        bindingGroup = new BindingGroup();
        Binding binding;
        binding = Bindings.createAutoBinding(UpdateStrategy.READ, this, BeanProperty.create(BeanProperty.create(PROP_DBSESSION), "resourceTypes"), resourcesTable, BeanProperty.create(ResourcesList.PROP_RESOURCETYPES));
        bindingGroup.addBinding(binding);
        binding = Bindings.createAutoBinding(UpdateStrategy.READ, this, BeanProperty.create(BeanProperty.create(PROP_FAMILY), Family.PROP_RESOURCES), resourcesTable, BeanProperty.create(JXXTable.PROP_ITEMS));
        bindingGroup.addBinding(binding);
        binding = Bindings.createAutoBinding(UpdateStrategy.READ, this, BeanProperty.create(PROP_FAMILY), resourcesInfo, BeanProperty.create(FamilyResourcesInfo.PROP_FAMILY));
        bindingGroup.addBinding(binding);
        binding = Bindings.createAutoBinding(UpdateStrategy.READ, this, BeanProperty.create("background"), resourcesInfo, BeanProperty.create("background"));
        bindingGroup.addBinding(binding);
    }

    public void addResource() {
        int newIndex = family.getResources().size();
        family.addNewResource(dbSession);
        assert family.getResources().size() == newIndex + 1;

        // the new resource is added at the end of the model
        // but, maybe, rows are sorted in another way
        int row = resourcesTable.convertRowIndexToView(newIndex);
        // automatically select the new row:
        resourcesTable.getSelectionModel().setSelectionInterval(row, row);
        // edit the first cell in the row immediately:
        resourcesTable.editRow(row);
    }

    public void removeSelectedResources() {
        resourcesTable.executeOnSelectedItems("JXXTable.remove", new ListAction<Resource>() {

            @Override
            public void performAction(int index, Resource resource) {
                dbSession.remove(resource);
                resource.setFamily(null);
            }
        });
    }

    @Override
    public void bind() {
        bindingGroup.bind();
        resourcesTable.bind();
        resourcesInfo.bind();
    }

    @Override
    public void unbind() {
        resourcesInfo.unbind();
        resourcesTable.unbind();
        bindingGroup.unbind();
    }
}
