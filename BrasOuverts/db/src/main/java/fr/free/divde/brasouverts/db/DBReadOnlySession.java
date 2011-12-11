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
package fr.free.divde.brasouverts.db;

import fr.free.divde.brasouverts.db.model.Country;
import fr.free.divde.brasouverts.db.model.ResourceType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class DBReadOnlySession {

    protected EntityManager entityManager;
    private List<Country> countries;
    private List<ResourceType> resourceTypes;

    public DBReadOnlySession() {
        entityManager = BrasOuvertsDB.getEntityManagerFactory().createEntityManager();
    }

    public <T> TypedQuery<T> createQuery(String string, Class<T> type) {
        if (!string.startsWith("SELECT")) {
            throw new IllegalArgumentException();
        }
        return entityManager.createQuery(string, type);
    }

    public <T> T getReference(Class<T> type, Object o) {
        return entityManager.getReference(type, o);
    }

    public void close() {
        entityManager.close();
        entityManager = null;
    }

    public List<Country> getCountries() {
        if (countries == null) {
            countries = Country.getCountries(this);
            countries.add(0, null);
        }
        return countries;
    }

    public List<ResourceType> getResourceTypes() {
        if (resourceTypes == null) {
            resourceTypes = ResourceType.getResourceTypes(this);
            resourceTypes.add(0, null);
        }
        return resourceTypes;
    }
}
