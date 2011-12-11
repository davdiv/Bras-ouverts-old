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

import fr.free.divde.brasouverts.db.DBReadOnlySession;
import fr.free.divde.brasouverts.db.model.Family;
import fr.free.divde.brasouverts.dbui.uicomponents.BindableComponent;
import lombok.Getter;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.openide.util.NbBundle;

public class FamilyContact extends javax.swing.JPanel implements BindableComponent {

    public void setDbSession(DBReadOnlySession newValue) {
        firePropertyChange(PROP_DBSESSION, dbSession, dbSession = newValue);
    }
    public final static String PROP_DBSESSION = "dbSession";
    @Getter
    protected DBReadOnlySession dbSession;

    public void setFamily(Family newValue) {
        firePropertyChange(PROP_FAMILY, family, family = newValue);
    }
    public final static String PROP_FAMILY = "family";
    @Getter
    protected Family family;

    /** Creates new form FamilyContact */
    public FamilyContact() {
        setName(NbBundle.getMessage(FamilyContact.class, "FamilyContact.title"));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        javax.swing.JPanel addressPanel = this.addressPanel;
        otherContactInfoPanel = new javax.swing.JPanel();
        telephoneLabel = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        telephoneField = new javax.swing.JTextField();
        emailField = new javax.swing.JTextField();

        setLayout(new org.jdesktop.swingx.VerticalLayout());

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${background}"), addressPanel, org.jdesktop.beansbinding.BeanProperty.create("background"));
        bindingGroup.addBinding(binding);

        addressPanel.setLayout(null);
        add(addressPanel);

        otherContactInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FamilyContact.class, "FamilyContact.otherContactInfoPanel.border.title"))); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${background}"), otherContactInfoPanel, org.jdesktop.beansbinding.BeanProperty.create("background"));
        bindingGroup.addBinding(binding);

        telephoneLabel.setText(org.openide.util.NbBundle.getMessage(FamilyContact.class, "FamilyContact.telephoneLabel.text")); // NOI18N

        emailLabel.setText(org.openide.util.NbBundle.getMessage(FamilyContact.class, "FamilyContact.emailLabel.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${family.telephone}"), telephoneField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${family.email}"), emailField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout otherContactInfoPanelLayout = new javax.swing.GroupLayout(otherContactInfoPanel);
        otherContactInfoPanel.setLayout(otherContactInfoPanelLayout);
        otherContactInfoPanelLayout.setHorizontalGroup(
            otherContactInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(otherContactInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(otherContactInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(telephoneLabel)
                    .addComponent(emailLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(otherContactInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(telephoneField, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                    .addComponent(emailField, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
                .addContainerGap())
        );
        otherContactInfoPanelLayout.setVerticalGroup(
            otherContactInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(otherContactInfoPanelLayout.createSequentialGroup()
                .addGroup(otherContactInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(telephoneLabel)
                    .addComponent(telephoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(otherContactInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(otherContactInfoPanel);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField emailField;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JPanel otherContactInfoPanel;
    private javax.swing.JTextField telephoneField;
    private javax.swing.JLabel telephoneLabel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
    private AddressPanel addressPanel;

    @Override
    public void bind() {
        if (bindingGroup == null) {
            addressPanel = new AddressPanel();
            initComponents();
            bindingGroup.addBinding(Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, this, BeanProperty.create(BeanProperty.create("family"), "address"), addressPanel, BeanProperty.create(AddressPanel.ADDRESS_PROP)));
            bindingGroup.addBinding(Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, this, BeanProperty.create(BeanProperty.create("dbSession"), "countries"), addressPanel, BeanProperty.create(AddressPanel.COUNTRIES_PROP)));
        }
        bindingGroup.bind();
        addressPanel.bind();
    }

    @Override
    public void unbind() {
        if (bindingGroup != null) {
            addressPanel.unbind();
            bindingGroup.unbind();
        }
    }
}