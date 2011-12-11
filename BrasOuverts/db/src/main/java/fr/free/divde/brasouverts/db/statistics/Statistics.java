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

import fr.free.divde.brasouverts.db.DBReadOnlySession;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.TypedQuery;
import lombok.Getter;

public class Statistics {

    private String queryString;
    private Map<String, Object> parameters;
    @Getter
    private DBReadOnlySession dbSession;
    protected TypedQuery<Long> queryObj;

    public Statistics(String queryString) {
        this.queryString = queryString;
    }

    public Statistics(String queryString, Map<String, Object> parameters) {
        this.queryString = queryString;
        this.parameters = parameters;
    }

    public void setDbSession(DBReadOnlySession dbSession) {
        this.dbSession = dbSession;
        queryObj = dbSession.createQuery(queryString, Long.class);
        if (parameters != null) {
            for (Entry<String, Object> entry : parameters.entrySet()) {
                queryObj.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    public Long getValue(DateInterval interval) {
        queryObj.setParameter("beginDate", interval.getBeginDate());
        queryObj.setParameter("endDate", interval.getEndDate());
        Long res = queryObj.getSingleResult();
        // with JPQL: sum(empty array) = null
        return res != null ? res : 0;
    }
}
