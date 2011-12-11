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

import fr.free.divde.brasouverts.dbui.family.FamilyEditorTopComponent;
import java.util.Collection;
import java.util.Set;
import javax.swing.SwingUtilities;
import org.openide.LifecycleManager;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;

@ServiceProvider(service = LifecycleManager.class, position = 1)
public class BrasOuvertsLifecycle extends LifecycleManager {

    @Override
    public void saveAll() {
        // call saveAll from the next lifeCycleManager
        Collection<LifecycleManager> managers = Lookup.getDefault().lookup(new Lookup.Template(LifecycleManager.class)).allInstances();
        for (LifecycleManager lm : managers) {
            if (!(lm instanceof BrasOuvertsLifecycle)) {
                lm.saveAll();
                return;
            }
        }
    }

    @Override
    public void exit() {
        TopComponentCloseClass close = new TopComponentCloseClass();
        if (SwingUtilities.isEventDispatchThread()) {
            close.run();
        } else {
            SwingUtilities.invokeLater(close);
        }
    }

    private static class TopComponentCloseClass implements Runnable {

        @Override
        public void run() {
            Set<TopComponent> tcs = TopComponent.getRegistry().getOpened();
            for (TopComponent tc : tcs) {
                if (tc instanceof FamilyEditorTopComponent) {
                    if (!tc.close()) {
                        // do not close if an editor was not closed successfully
                        return;
                    }
                }
            }

            // call exit from the next lifeCycleManager
            Collection<LifecycleManager> managers = Lookup.getDefault().lookup(new Lookup.Template(LifecycleManager.class)).allInstances();
            for (LifecycleManager lm : managers) {
                if (!(lm instanceof BrasOuvertsLifecycle)) {
                    lm.exit();
                    return;
                }
            }

        }
    }
}
