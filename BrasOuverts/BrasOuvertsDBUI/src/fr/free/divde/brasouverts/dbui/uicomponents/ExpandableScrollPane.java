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
import javax.swing.JScrollPane;

public class ExpandableScrollPane extends JScrollPane {

    public ExpandableScrollPane() {
    }

    public ExpandableScrollPane(Component component) {
        super(component);
    }

    @Override
    public boolean isValidateRoot() {
        return false;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension res = super.getPreferredSize();
        if (isPreferredSizeSet()) {
            return res;
        } else {
            // get the preferred size of the view, and do not display headers if the size of the content is null:
            Component view = getViewport().getView();
            Dimension viewSize = view != null ? view.getPreferredSize() : null;
            if (view == null || viewSize.width <= 0 || viewSize.height <= 0) {
                return new Dimension(0, 0);
            } else {
                return new Dimension(res.width + getVerticalScrollBar().getWidth(), res.height + getHorizontalScrollBar().getHeight());
            }
        }
    }
}
