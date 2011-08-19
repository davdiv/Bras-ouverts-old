/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.free.divde.brasouverts.dbui.uicomponents;

import java.util.Date;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author david-emmanuel
 */
public class AgeConverter extends Converter<Date, Integer> {

    private final static long MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
    private final static long MILLISECONDS_IN_YEAR = MILLISECONDS_IN_DAY * 365;

    @Override
    public Integer convertForward(Date date) {
        if (date == null) {
            return null;
        }
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - date.getTime();
        long diffInYears = diff / MILLISECONDS_IN_YEAR;
        return (int) diffInYears;
    }

    @Override
    public Date convertReverse(Integer t) {
        throw new UnsupportedOperationException();
    }
}
