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
package fr.free.divde.brasouverts.db.statistics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import org.openide.util.NbBundle;

public class DateInterval {

    public static enum RelationToCalendar {

        SINGLE_WHOLE_YEAR(true, true, true, false, false),
        SEVERAL_WHOLE_YEARS(true, false, true, false, false),
        SINGLE_WHOLE_MONTH(false, true, true, true, false),
        SEVERAL_WHOLE_MONTHS_IN_SAME_YEAR(false, true, true, false, false),
        SEVERAL_WHOLE_MONTHS_IN_DIFFERENT_YEARS(false, false, true, false, false),
        SINGLE_DAY(false, true, false, true, true),
        SEVERAL_DAYS_IN_SAME_MONTH(false, true, false, true, false),
        SEVERAL_DAYS_IN_SAME_YEAR(false, true, false, false, false),
        SEVERAL_DAYS_IN_DIFFERENT_YEARS(false, false, false, false, false);

        private RelationToCalendar(boolean wholeYear, boolean sameYear, boolean wholeMonth, boolean sameMonth, boolean sameDay) {
            assert (wholeYear ? wholeMonth : true); // wholeYear implies wholeMonth
            assert (sameDay ? sameMonth : true); // sameDay implies sameMonth
            assert (sameMonth ? sameYear : true); // sameMonth implies sameYear
            this.wholeYear = wholeYear;
            this.sameYear = sameYear;
            this.wholeMonth = wholeMonth;
            this.sameMonth = sameMonth;
            this.sameDay = sameDay;
        }
        @Getter
        private boolean wholeYear;
        @Getter
        private boolean sameYear;
        @Getter
        private boolean wholeMonth;
        @Getter
        private boolean sameMonth;
        @Getter
        private boolean sameDay;
    }
    private long beginDate;
    private long endDate;
    @Getter
    private RelationToCalendar relationToCalendar;
    private String str;

    public DateInterval(long beginDate, long endDate) {
        if (beginDate > endDate) {
            throw new IllegalArgumentException("beginDate > endDate");
        }
        this.relationToCalendar = getRelationToCalendar(beginDate, endDate);
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    private static RelationToCalendar getRelationToCalendar(long beginDate, long endDate) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(beginDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(endDate);
        if (calendar1.getActualMinimum(Calendar.DAY_OF_MONTH) == calendar1.get(Calendar.DAY_OF_MONTH)
                && calendar2.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) {
            // ignore days as they match month boundaries
            if (calendar1.getActualMinimum(Calendar.MONTH) == calendar1.get(Calendar.MONTH)
                    && calendar2.getActualMaximum(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {
                // ignore months as they match year boundaries
                if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
                    return RelationToCalendar.SINGLE_WHOLE_YEAR;
                } else {
                    return RelationToCalendar.SEVERAL_WHOLE_YEARS;
                }
            } else {
                // does not match a whole calendar year
                if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
                    // a part of the same year
                    if (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {
                        return RelationToCalendar.SINGLE_WHOLE_MONTH;
                    } else {
                        return RelationToCalendar.SEVERAL_WHOLE_MONTHS_IN_SAME_YEAR;
                    }
                } else {
                    return RelationToCalendar.SEVERAL_WHOLE_MONTHS_IN_DIFFERENT_YEARS;
                }
            }
        } else {
            // does not match a whole month
            if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
                if (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {
                    if (calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) {
                        return RelationToCalendar.SINGLE_DAY;
                    } else {
                        return RelationToCalendar.SEVERAL_DAYS_IN_SAME_MONTH;
                    }
                } else {
                    return RelationToCalendar.SEVERAL_DAYS_IN_SAME_YEAR;
                }
            } else {
                return RelationToCalendar.SEVERAL_DAYS_IN_DIFFERENT_YEARS;
            }
        }
    }

    private String format() {
        Date date1 = getBeginDate();
        Date date2 = getEndDate();
        String baseMsgId = "DateInterval." + relationToCalendar.name() + ".";
        DateFormat date1Format = new SimpleDateFormat(NbBundle.getMessage(DateInterval.class, baseMsgId + "format1"));
        DateFormat date2Format = new SimpleDateFormat(NbBundle.getMessage(DateInterval.class, baseMsgId + "format2"));
        String strDate1 = date1Format.format(date1);
        String strDate2 = date2Format.format(date2);
        return NbBundle.getMessage(DateInterval.class, baseMsgId + "whole", strDate1, strDate2);
    }

    public Date getBeginDate() {
        return new Date(beginDate);
    }

    public Date getEndDate() {
        return new Date(endDate);
    }

    @Override
    public String toString() {
        if (str == null) {
            str = format();
        }
        return str;
    }

    public List<DateInterval> split() {
        if (relationToCalendar.wholeMonth && !relationToCalendar.wholeYear && !relationToCalendar.sameMonth) {
            // several months
            List<DateInterval> dateIntervals = new ArrayList<DateInterval>();
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar1.setTimeInMillis(beginDate);
            while (calendar1.getTimeInMillis() < endDate) {
                calendar2.setTimeInMillis(calendar1.getTimeInMillis());
                calendar2.set(Calendar.DAY_OF_MONTH, calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
                dateIntervals.add(new DateInterval(calendar1.getTimeInMillis(), calendar2.getTimeInMillis()));
                calendar1.set(Calendar.MONTH, calendar1.get(Calendar.MONTH) + 1); // get the next month
            }
            return dateIntervals;
        } else {
            return Arrays.asList(this);
        }
    }
}
