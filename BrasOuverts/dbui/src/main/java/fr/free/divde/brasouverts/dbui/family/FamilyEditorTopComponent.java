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

import fr.free.divde.brasouverts.db.listeners.ChangeListener.ChangeType;
import fr.free.divde.brasouverts.dbui.uicomponents.TaskPaneWrapper;
import fr.free.divde.brasouverts.db.DBReadWriteSession;
import fr.free.divde.brasouverts.db.check.FamilyCheck;
import fr.free.divde.brasouverts.db.delete.DeleteFamily;
import fr.free.divde.brasouverts.db.listeners.FamilyChangeListener;
import fr.free.divde.brasouverts.db.model.Family;
import fr.free.divde.brasouverts.dbui.actions.DeleteCookie;
import fr.free.divde.brasouverts.dbui.uicomponents.BindableComponent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.CharConversionException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import lombok.Getter;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.actions.SaveAction;
import org.openide.cookies.SaveCookie;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.CloneableTopComponent;
import org.openide.windows.TopComponent;
import org.openide.xml.XMLUtil;

@ConvertAsProperties(dtd = "-//fr.free.divde.brasouverts.dbui.family//FamilyEditor//EN",
autostore = false)
@TopComponent.Description(preferredID = "FamilyEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@NbBundle.Messages({
    "FamilyEditorTopComponent.DeleteFamilyConfirmationMessage=Are you sure you want to completely remove family {0} from the database?",
    "FamilyEditorTopComponent.DeleteFamilyConfirmationTitle=Delete confirmation"
})
public class FamilyEditorTopComponent extends CloneableTopComponent implements BindableComponent, SaveCookie {

    public void setFamily(Family newValue) {
        firePropertyChange(PROP_FAMILY, family, family = newValue);
    }
    public static final String PROP_FAMILY = "family";
    @Getter
    private Family family;
    private FamilyChangeListener familyChange;

    public void setDbSession(DBReadWriteSession newValue) {
        firePropertyChange(PROP_DBSESSION, dbSession, dbSession = newValue);
    }
    public static final String PROP_DBSESSION = "dbSession";
    @Getter
    private DBReadWriteSession dbSession;
    private FamilyGeneralInfo familyGeneralInfo;
    private FamilyContact familyContact;
    private FamilyMembers familyMembers;
    private FamilyResources familyResources;
    private FamilyVisits familyVisits;
    private BindingGroup bindingGroup;
    private InstanceContent content = new InstanceContent();
    @Getter
    private boolean changed = false;
    private DeleteCookie deleteCookie = new DeleteCookie() {

        @Override
        public void delete() {
            int res = JOptionPane.showConfirmDialog(FamilyEditorTopComponent.this, Bundle.FamilyEditorTopComponent_DeleteFamilyConfirmationMessage(getName()), Bundle.FamilyEditorTopComponent_DeleteFamilyConfirmationTitle(), JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                DeleteFamily deleteFamily = new DeleteFamily(dbSession);
                family.accept(deleteFamily);
                dbSession.save();
                closeAllClones();
            }
        }
    };

    public FamilyEditorTopComponent() {
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        try {
            if (changed) {
                setHtmlDisplayName("<html><b>" + XMLUtil.toElementContent(name) + "</b></html>");
            } else {
                setHtmlDisplayName("<html>" + XMLUtil.toElementContent(name) + "</html>");
            }
        } catch (CharConversionException ex) {
            // this should never happen
            Exceptions.printStackTrace(ex);
        }
    }

    public void setChanged(boolean changed) {
        // TODO: fix this potential issue with familyChange.bind/unbind called both here and in the bind/unbind methods,
        // potentially leading to familyChange being bound even when unbind was called
        if (this.changed != changed) {
            this.changed = changed;
            if (changed) {
                content.add(this);
                familyChange.unbind();
            } else {
                content.remove(this);
                familyChange.bind();
            }
            setName(getName()); // update display name
        }
    }

    private void initComponent() {
        familyChange = new FamilyChangeListener() {

            @Override
            protected void change(Object source, ChangeType changeType) {
                dbSession.notifyChanged();
            }
        };
        bindingGroup = new BindingGroup();
        Binding binding;
        Property familyProperty = BeanProperty.create("family");
        Property dbSessionProperty = BeanProperty.create("dbSession");
        binding = Bindings.createAutoBinding(UpdateStrategy.READ, this, BeanProperty.create(familyProperty, Family.PROP_NAME), this, BeanProperty.create("name"));
        binding.setSourceNullValue(NbBundle.getMessage(FamilyEditorTopComponent.class, "FamilyEditorTopComponent.newFamilyTitle"));
        bindingGroup.addBinding(binding);
        JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();

        familyGeneralInfo = new FamilyGeneralInfo();
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, this, familyProperty, familyGeneralInfo, familyProperty));
        familyContact = new FamilyContact();
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, this, familyProperty, familyContact, familyProperty));
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, this, dbSessionProperty, familyContact, dbSessionProperty));
        familyMembers = new FamilyMembers();
        familyMembers.setCollapsed(true);
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, this, familyProperty, familyMembers, familyProperty));
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, this, dbSessionProperty, familyMembers, dbSessionProperty));
        familyResources = new FamilyResources();
        familyResources.setCollapsed(true);
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, this, familyProperty, familyResources, familyProperty));
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, this, dbSessionProperty, familyResources, dbSessionProperty));
        familyVisits = new FamilyVisits();
        familyVisits.setCollapsed(true);
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, this, familyProperty, familyVisits, familyProperty));
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, this, dbSessionProperty, familyVisits, dbSessionProperty));
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, this, familyProperty, familyChange, BeanProperty.create("rootObject")));
        bindingGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, this, BeanProperty.create(dbSessionProperty, DBReadWriteSession.PROP_CHANGED), this, BeanProperty.create("changed")));
        taskPaneContainer.add((Component) new TaskPaneWrapper(familyGeneralInfo, false));
        taskPaneContainer.add((Component) new TaskPaneWrapper(familyContact, true));
        taskPaneContainer.add((Component) familyMembers);
        taskPaneContainer.add((Component) familyResources);
        taskPaneContainer.add((Component) familyVisits);
        setLayout(new BorderLayout());
        JScrollPane scrollpane = new JScrollPane(taskPaneContainer);
        add(scrollpane, BorderLayout.CENTER);
        content.add(deleteCookie);
        associateLookup(new AbstractLookup(content));
    }

    @Override
    public void bind() {
        if (bindingGroup == null) {
            initComponent();
        }
        bindingGroup.bind();
        familyContact.bind();
        familyGeneralInfo.bind();
        familyMembers.bind();
        familyResources.bind();
        familyVisits.bind();
        familyChange.bind();
    }

    @Override
    public void unbind() {
        if (bindingGroup != null) {
            familyChange.unbind();
            familyContact.unbind();
            familyGeneralInfo.unbind();
            familyMembers.unbind();
            familyResources.unbind();
            familyVisits.unbind();
            bindingGroup.unbind();
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        p.setProperty("familyId", family.getId().toString());
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        Long familyId = Long.decode(p.getProperty("familyId"));
        initOrCloneForFamily(familyId);
    }

    @Override
    protected CloneableTopComponent createClonedObject() {
        FamilyEditorTopComponent clone = new FamilyEditorTopComponent();
        clone.initByCloning(this);
        return clone;
    }

    @Override
    protected boolean closeLast() {
        if (changed) {
            int res = JOptionPane.showConfirmDialog(this, NbBundle.getMessage(FamilyEditorTopComponent.class, "FamilyEditorTopComponent.saveWhenClosingMessage", this.getName()), NbBundle.getMessage(FamilyEditorTopComponent.class, "FamilyEditorTopComponent.saveWhenClosingTitle"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (res == JOptionPane.YES_OPTION) {
                // should first save
                try {
                    save();
                    if (changed) {
                        // saving succeeded
                        return true;
                    }
                } catch (IOException ex) {
                    NotifyDescriptor nd = new NotifyDescriptor.Message(NbBundle.getMessage(SaveAction.class, "EXC_notsaved", this.getName(), ex.getMessage()), NotifyDescriptor.ERROR_MESSAGE);
                    DialogDisplayer.getDefault().notifyLater(nd);
                }
                return false; // saving did not work
            } else if (res == JOptionPane.CANCEL_OPTION) {
                // do not close the editor
                return false;
            }
        }
        return true;
    }

    public void initOrCloneForFamily(Long familyId) {
        FamilyEditorTopComponent existing = getExistingEditorForFamily(familyId);
        if (existing != null) {
            initByCloning(existing);
        } else {
            initWithFamily(familyId);
        }
    }

    public void initByCloning(FamilyEditorTopComponent existing) {
        setDbSession(existing.dbSession);
        setFamily(existing.family);
        bind();
    }

    public void initWithFamily(Long familyId) {
        Family preparedFamily;
        if (dbSession == null) {
            setDbSession(new DBReadWriteSession());
        }
        if (familyId == null) {
            preparedFamily = new Family();
            dbSession.persist(preparedFamily);
        } else {
            preparedFamily = dbSession.getReference(Family.class, familyId);
        }
        setFamily(preparedFamily);
        bind();
    }

    public static FamilyEditorTopComponent getExistingEditorForFamily(Long familyId) {
        if (familyId != null) {
            FamilyEditorTopComponent res;
            Iterator<TopComponent> it = TopComponent.getRegistry().getOpened().iterator();
            while (it.hasNext()) {
                TopComponent component = it.next();
                if (component instanceof FamilyEditorTopComponent) {
                    res = (FamilyEditorTopComponent) component;
                    if (familyId.equals(res.family.getId())) {
                        return res;
                    }
                }
            }
        }
        return null;
    }

    public static FamilyEditorTopComponent getEditorForFamily(Long familyId) {
        FamilyEditorTopComponent res;
        res = getExistingEditorForFamily(familyId);
        if (res != null) {
            return res;
        }
        // not found in existing windows, or a null familyId
        res = new FamilyEditorTopComponent();
        res.initWithFamily(familyId);
        return res;
    }

    public static FamilyEditorTopComponent openEditorForFamily(Long familyId) {
        FamilyEditorTopComponent res = getEditorForFamily(familyId);
        res.open();
        res.requestActive();
        return res;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void save() throws IOException {
        FamilyCheck familyCheck = new FamilyCheck();
        family.accept(familyCheck);
        if (familyCheck.containsErrors()) {
            throw new IOException(familyCheck.toString());
        }
        dbSession.save();
    }

    public void closeAllClones() {
        Enumeration<CloneableTopComponent> it = getReference().getComponents();
        while (it.hasMoreElements()) {
            it.nextElement().close();
        }
    }
}
