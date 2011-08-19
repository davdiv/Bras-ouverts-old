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
package fr.free.divde.brasouverts.dbui.actions;

import fr.free.divde.brasouverts.dbui.family.FamilyEditorTopComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "File",
id = "fr.free.divde.brasouverts.dbui.NewFamily")
@ActionRegistration(displayName = "#CTL_NewFamily")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1200)
})
@Messages("CTL_NewFamily=New family")
public final class NewFamilyAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        FamilyEditorTopComponent.openEditorForFamily(null);
    }
}
