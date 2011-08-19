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
package fr.free.divde.brasouverts.db.model;

import fr.free.divde.brasouverts.db.DBReadOnlySession;
import fr.free.divde.brasouverts.db.check.ModelCheck;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.TypedQuery;
import lombok.Getter;

@Entity
public class Country extends DBEntity<Integer> {

    public static List<Country> getCountries(DBReadOnlySession dbSession) {
        TypedQuery<Country> query = dbSession.createQuery("SELECT c FROM Country c ORDER BY c.langFR", Country.class);
        return query.getResultList();
    }

    @Override
    public void check(ModelCheck check) {
        // no check for now
    }

    private static interface Language {

        String getTranslation(Country country);
    }

    private static Map<String, Language> initLanguageMap() {
        HashMap<String, Language> res = new HashMap<String, Language>(7);
        res.put("cs", new Language() {

            @Override
            public String getTranslation(Country country) {
                return country.langCS;
            }
        });
        res.put("de", new Language() {

            @Override
            public String getTranslation(Country country) {
                return country.langDE;
            }
        });
        res.put("en", new Language() {

            @Override
            public String getTranslation(Country country) {
                return country.langEN;
            }
        });
        res.put("es", new Language() {

            @Override
            public String getTranslation(Country country) {
                return country.langES;
            }
        });
        res.put("fr", new Language() {

            @Override
            public String getTranslation(Country country) {
                return country.langFR;
            }
        });
        res.put("it", new Language() {

            @Override
            public String getTranslation(Country country) {
                return country.langIT;
            }
        });
        res.put("nl", new Language() {

            @Override
            public String getTranslation(Country country) {
                return country.langNL;
            }
        });
        return res;
    }
    private static final Map<String, Language> languageMap = initLanguageMap();
    private static final long serialVersionUID = 1L;
    @Getter
    private String alpha2;
    @Getter
    private String alpha3;
    @Getter
    private String langCS;
    @Getter
    private String langDE;
    @Getter
    private String langEN;
    @Getter
    private String langES;
    @Getter
    private String langFR;
    @Getter
    private String langIT;
    @Getter
    private String langNL;

    public String getLang(String languageCode) {
        Language language = languageMap.get(languageCode);
        if (language == null) {
            return getLangEN();
        }
        return language.getTranslation(this);
    }

    @Override
    public String toString() {
        String code = Locale.getDefault().getLanguage();
        return getLang(code);
    }
}
