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

import java.util.Date;
import java.util.Map;

public class AgeStatistics extends Statistics {

    private Map<String, Integer> ageProperties;

    public AgeStatistics(String queryString, Map<String, Integer> ageProperties) {
        super(queryString);
        this.ageProperties = ageProperties;
    }

    @Override
    public Long getValue(DateInterval interval) {
        for (Map.Entry<String, Integer> entry : ageProperties.entrySet()) {
            String key = entry.getKey();
            long value = entry.getValue();
            long birthDate = interval.getEndDate().getTime() - value * 1000 * 60 * 60 * 24 * 365;
            queryObj.setParameter(key, new Date(birthDate));
        }
        return super.getValue(interval);
    }
}
