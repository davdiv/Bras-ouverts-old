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

import fr.free.divde.brasouverts.db.initdb.InitDB;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.prefs.Preferences;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbPreferences;

public class BrasOuvertsDB {

    private static EntityManagerFactory entityManagerFactory;

    public static void resetConnectionPreferences() {
        Preferences prefs = NbPreferences.forModule(BrasOuvertsDB.class);
        prefs.putBoolean("defined", true);
        FileObject dbFolder;
        File dbFile;
        try {
            dbFolder = FileUtil.getConfigRoot().createFolder("BrasOuvertsDB");
        } catch (IOException ex) {
            // the folder may already exist
            dbFolder = FileUtil.getConfigRoot().getFileObject("BrasOuvertsDB");
        }
        dbFile = new File(FileUtil.toFile(dbFolder), "db");
        prefs.put("javax.persistence.jdbc.url", "jdbc:hsqldb:" + dbFile.toURI().toString());
        prefs.put("javax.persistence.jdbc.driver", "org.hsqldb.jdbc.JDBCDriver");
        prefs.put("javax.persistence.jdbc.user", "");
        prefs.put("javax.persistence.jdbc.password", "");
    }

    public static void checkConnectionPreferences() {
        Preferences pref = NbPreferences.forModule(BrasOuvertsDB.class);
        if (!pref.getBoolean("defined", false)) {
            resetConnectionPreferences();
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            checkConnectionPreferences();
            Preferences pref = NbPreferences.forModule(BrasOuvertsDB.class);
            Properties properties = new Properties();
            properties.put("javax.persistence.jdbc.driver", pref.get("javax.persistence.jdbc.driver", ""));
            properties.put("javax.persistence.jdbc.url", pref.get("javax.persistence.jdbc.url", ""));
            properties.put("javax.persistence.jdbc.password", pref.get("javax.persistence.jdbc.password", ""));
            properties.put("javax.persistence.jdbc.user", pref.get("javax.persistence.jdbc.user", ""));
            properties.put("eclipselink.ddl-generation", pref.get("eclipselink.ddl-generation", "create-tables"));
            EntityManagerFactory res = Persistence.createEntityManagerFactory("BrasOuvertsDBPU", properties);
            if (InitDB.checkDB(res)) {
                entityManagerFactory = res;
            }
        }
        return entityManagerFactory;
    }
}
