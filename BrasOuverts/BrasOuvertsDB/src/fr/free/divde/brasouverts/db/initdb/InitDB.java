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
package fr.free.divde.brasouverts.db.initdb;

import fr.free.divde.brasouverts.db.model.DBVersion;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import org.openide.util.Exceptions;

public class InitDB {

    public static boolean checkDB(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<DBVersion> dbVersionQuery = entityManager.createQuery("SELECT v FROM DBVersion v", DBVersion.class);
            List<DBVersion> dbVersion = dbVersionQuery.getResultList();
            if (dbVersion.isEmpty()) {
                // initialize database
                entityManager.getTransaction().begin();
                Connection connection = entityManager.unwrap(Connection.class);
                importCountries(connection);
                importResourceTypes(connection);
                DBVersion currentDBVersion = new DBVersion();
                entityManager.persist(currentDBVersion);
                entityManager.flush();
                entityManager.getTransaction().commit();
                return true;
            } else if (dbVersion.size() == 1) {
                DBVersion currentDBVersion = dbVersion.get(0);
                return currentDBVersion.isCurrentVersion();
            } else {
                return false;
            }
        } finally {
            entityManager.close();
        }
    }

    public static void importCountries(Connection connection) {
        executeSql(connection, "countries.sql");
    }

    public static void importResourceTypes(Connection connection) {
        executeSql(connection, "resourcetypes.sql");
    }

    private static void executeSql(Connection connection, String sqlFile) {
        InputStreamReader reader = null;
        try {
            InputStream stream = InitDB.class.getClassLoader().getResourceAsStream(InitDB.class.getPackage().getName().replace(".", "/") + "/" + sqlFile);
            reader = new InputStreamReader(stream, "UTF-8");
            SqlFile file = new SqlFile(reader, sqlFile, System.out, "UTF-8", false, null);
            file.setConnection(connection);
            file.execute();
        } catch (SqlToolError ex) {
            Exceptions.printStackTrace(ex);
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
