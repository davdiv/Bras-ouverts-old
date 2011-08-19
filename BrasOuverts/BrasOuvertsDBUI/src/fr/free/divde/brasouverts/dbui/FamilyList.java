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
package fr.free.divde.brasouverts.dbui;

import fr.free.divde.brasouverts.db.model.Family;
import fr.free.divde.brasouverts.dbui.uicomponents.JXXTable;
import fr.free.divde.brasouverts.dbui.uicomponents.StringCellEditor;

public class FamilyList extends JXXTable<Family> {

    @Override
    protected Column[] createColumns() {
        Column[] res = new JXXTable.Column[]{
            new Column("FamilyList.Name", Family.PROP_NAME, String.class, new StringCellEditor(), 150)};
        return res;
    }
}
