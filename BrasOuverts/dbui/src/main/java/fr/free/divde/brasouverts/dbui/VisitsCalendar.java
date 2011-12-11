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

import fr.free.divde.brasouverts.db.model.Visit;
import fr.free.divde.brasouverts.dbui.uicomponents.BindableComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import lombok.Getter;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;

public class VisitsCalendar extends JXMonthView implements BindableComponent {

    public static final String PROP_VISITS = "visits";
    @Getter
    protected ObservableList<Visit> visits = ObservableCollections.observableList(Collections.EMPTY_LIST);
    private boolean bound = false;
    public static final String PROP_SELECTED_INDEX = "selectedIndex";
    @Getter
    private int selectedIndex = -1;

    public void setSelectedIndex(int newValue) {
        Visit visit = newValue < 0 ? null : visits.get(newValue);
        Date date = visit != null ? visit.getVisitDate() : null;
        if (date != null) {
            setSelectionDate(date);
            ensureDateVisible(date);
        }
        firePropertyChange(PROP_SELECTED_INDEX, selectedIndex, selectedIndex = newValue);
    }
    private ObservableListListener listListener = new ObservableListListener() {

        @Override
        public void listElementsAdded(ObservableList ol, int i, int length) {
            int afterLastIndex = i + length;
            ListIterator<Visit> it = ol.listIterator(i);
            while (it.hasNext() && it.nextIndex() < afterLastIndex) {
                addListenerOnItem(it.next());
            }
        }

        @Override
        public void listElementsRemoved(ObservableList ol, int i, List list) {
            removeListenerOnItems(list);
        }

        @Override
        public void listElementReplaced(ObservableList ol, int i, Object o) {
            removeListenerOnItem((Visit) o);
            addListenerOnItem((Visit) ol.get(i));
        }

        @Override
        public void listElementPropertyChanged(ObservableList ol, int i) {
            // not reliable
        }
    };
    private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (Visit.PROP_VISIT_DATE.equals(evt.getPropertyName())) {
                Date oldValue = (Date) evt.getOldValue();
                if (oldValue != null) {
                    removeFlaggedDates(oldValue);
                }
                Date newValue = (Date) evt.getNewValue();
                if (newValue != null) {
                    addFlaggedDates(newValue);
                }
            }
        }
    };

    public VisitsCalendar() {
        setTraversable(true);
        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int index = -1;
                Date selectedDate = getSelectionDate();
                if (selectedDate != null && isFlaggedDate(selectedDate)) {
                    index = findVisit(selectedDate);
                }
                setSelectedIndex(index);
            }
        });
    }
    
    private int findVisit(Date date) {
        ListIterator<Visit> it = visits.listIterator();
        while (it.hasNext()) {
            Visit visit = it.next();
            Date visitDate = visit.getVisitDate();
            if (visitDate != null && visitDate.equals(date)) {
                return it.nextIndex() - 1;
            }
        }
        return -1;
    }

    public void setVisits(ObservableList<Visit> newValue) {
        if (newValue == null) {
            return;
        }
        boolean wasBound = bound;
        unbind();
        firePropertyChange(PROP_VISITS, visits, visits = newValue);
        if (wasBound) {
            bind();
        }
    }

    @Override
    public void bind() {
        if (!bound) {
            clearFlaggedDates();
            addListenerOnItems(visits);
            visits.addObservableListListener(listListener);
            bound = true;
        }
    }

    @Override
    public void unbind() {
        if (bound) {
            visits.removeObservableListListener(listListener);
            removeListenerOnItems(visits);
            bound = false;
        }
    }

    private void addListenerOnItems(List<Visit> list) {
        for (Visit item : list) {
            addListenerOnItem(item);
        }
    }

    private void removeListenerOnItems(List<Visit> list) {
        for (Visit item : list) {
            removeListenerOnItem(item);
        }
    }

    private void addListenerOnItem(Visit item) {
        item.addPropertyChangeListener(propertyChangeListener);
        Date date = item.getVisitDate();
        if (date != null) {
            addFlaggedDates(date);
        }
    }

    private void removeListenerOnItem(Visit item) {
        item.removePropertyChangeListener(propertyChangeListener);
        Date date = item.getVisitDate();
        if (date != null) {
            removeFlaggedDates(date);
        }
    }
}
