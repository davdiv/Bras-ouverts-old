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

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
public class DBVersion {

    public static int currentMajorVersion = 0;
    public static int currentMinorVersion = 1;
    @Getter
    @Setter
    private int majorVersion = currentMajorVersion;
    @Getter
    @Setter
    private int minorVersion = currentMinorVersion;
    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Id
    private Date date = new Date();

    public boolean isCurrentVersion() {
        return majorVersion == currentMajorVersion && minorVersion == currentMinorVersion;
    }

    public boolean isPreviousVersion() {
        return majorVersion < currentMajorVersion || (majorVersion == currentMajorVersion && minorVersion < currentMinorVersion);
    }

    @Override
    public String toString() {
        return majorVersion + "." + minorVersion;
    }
}
