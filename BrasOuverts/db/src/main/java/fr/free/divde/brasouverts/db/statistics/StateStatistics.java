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

import fr.free.divde.brasouverts.db.model.Gender;
import java.util.HashMap;
import java.util.Map;

public class StateStatistics {

    private static String registeredFamily(String varName) {
        return "((" + varName + ".subscriptionBeginDate <= :endDate) AND (" + varName + ".subscriptionEndDate IS NULL OR " + varName + ".subscriptionEndDate >= :beginDate))";
    }

    private static String byMinDRTL(String familyVarName) {
        return " AND (" + familyVarName + ".dailyRemainingToLiveByPerson >= :minDRTL)";
    }

    private static String byDRTL(String familyVarName) {
        return byMinDRTL(familyVarName) + " AND (" + familyVarName + ".dailyRemainingToLiveByPerson < :maxDRTL)";
    }

    private static String byUnknownDRTL(String familyVarName) {
        return " AND (" + familyVarName + ".dailyRemainingToLiveByPerson IS NULL)";
    }
    private static final String queryNbRegisteredFamilies = "SELECT count(f) FROM Family f WHERE " + registeredFamily("f");
    private static final String queryNbRegisteredRecipients = "SELECT count(p) FROM Person p WHERE " + registeredFamily("p.family") + " AND (p.birthDate IS NULL OR p.birthDate <= :endDate)";
    private static final String queryNbHelpedRecipients = "SELECT sum(v.nbPackets) FROM Visit v INNER JOIN v.beneficiaryFamily.people p WHERE v.visitDate >= :beginDate AND v.visitDate <= :endDate AND (p.birthDate IS NULL OR p.birthDate <= v.visitDate)";
    private static final String queryNbHelpedRecipientsByMinAge = queryNbHelpedRecipients + " AND (v.visitDate - p.birthDate >= :minAge)";
    private static final String queryNbHelpedRecipientsByAge = queryNbHelpedRecipientsByMinAge + " AND (v.visitDate - p.birthDate < :maxAge)";
    private static final String byMinAge = " AND (p.birthDate <= :maxBirthDate)";
    private static final String byAge = byMinAge + " AND (p.birthDate > :minBirthDate)";
    private static final String byUnknownAge = " AND (p.birthDate IS NULL)";
    private static final String byGender = " AND (p.gender = :gender)";
    private static final String byUnknownGender = " AND (p.gender IS NULL)";

    public static Statistics queryNbRegisteredFamilies() {
        return new Statistics(queryNbRegisteredFamilies);
    }

    public static Statistics queryNbRegisteredRecipients() {
        return new Statistics(queryNbRegisteredRecipients);
    }

    public static Statistics queryNbHelpedRecipients() {
        return new Statistics(queryNbHelpedRecipients);
    }

    public static Statistics queryNbRegisteredRecipientsByAge(int minAge, int maxAge) {
        HashMap<String, Integer> ageProperties = new HashMap<String, Integer>(2);
        ageProperties.put("maxBirthDate", minAge);
        ageProperties.put("minBirthDate", maxAge);
        return new AgeStatistics(queryNbRegisteredRecipients + byAge, ageProperties);
    }

    public static Statistics queryNbRegisteredRecipientsByMinAge(int minAge) {
        HashMap<String, Integer> ageProperties = new HashMap<String, Integer>(2);
        ageProperties.put("maxBirthDate", minAge);
        return new AgeStatistics(queryNbRegisteredRecipients + byMinAge, ageProperties);
    }

    public static Statistics queryNbRegisteredRecipientsByUnknownAge() {
        return new Statistics(queryNbRegisteredRecipients + byUnknownAge);
    }

    public static Statistics queryNbRegisteredRecipientsByDRTL(float minDRTL, float maxDRTL) {
        HashMap<String, Object> drtlProperties = new HashMap<String, Object>(2);
        drtlProperties.put("minDRTL", minDRTL);
        drtlProperties.put("maxDRTL", maxDRTL);
        return new Statistics(queryNbRegisteredRecipients + byDRTL("p.family"), drtlProperties);
    }

    public static Statistics queryNbRegisteredRecipientsByMinDRTL(float minDRTL) {
        HashMap<String, Object> drtlProperties = new HashMap<String, Object>(1);
        drtlProperties.put("minDRTL", minDRTL);
        return new Statistics(queryNbRegisteredRecipients + byMinDRTL("p.family"), drtlProperties);
    }

    public static Statistics queryNbRegisteredRecipientsByUnknownDRTL() {
        return new Statistics(queryNbRegisteredRecipients + byUnknownDRTL("p.family"));
    }

    public static Statistics queryNbRegisteredRecipientsByGender(Gender gender) {
        if (gender == null) {
            return new Statistics(queryNbRegisteredRecipients + byUnknownGender);
        } else {
            Map<String, Object> genderProperties = new HashMap<String, Object>(1);
            genderProperties.put("gender", gender);
            return new Statistics(queryNbRegisteredRecipients + byGender, genderProperties);
        }
    }

    public static Statistics queryNbHelpedRecipientsByMinAge(int minAge) {
        Map<String, Object> properties = new HashMap<String, Object>(1);
        long longMinAge = minAge;
        longMinAge *= 365;
        properties.put("minAge", longMinAge);
        return new Statistics(queryNbHelpedRecipientsByMinAge, properties);
    }

    public static Statistics queryNbHelpedRecipientsByAge(int minAge, int maxAge) {
        Map<String, Object> properties = new HashMap<String, Object>(2);
        long longMinAge = minAge;
        long longMaxAge = maxAge;
        longMinAge *= 365;
        longMaxAge *= 365;
        properties.put("minAge", longMinAge);
        properties.put("maxAge", longMaxAge);
        return new Statistics(queryNbHelpedRecipientsByAge, properties);
    }

    public static Statistics queryNbHelpedRecipientsByUnknownAge() {
        return new Statistics(queryNbHelpedRecipients + byUnknownAge);
    }

    public static Statistics queryNbHelpedRecipientsByGender(Gender gender) {
        if (gender == null) {
            return new Statistics(queryNbHelpedRecipients + byUnknownGender);
        } else {
            Map<String, Object> genderProperties = new HashMap<String, Object>(1);
            genderProperties.put("gender", gender);
            return new Statistics(queryNbHelpedRecipients + byGender, genderProperties);
        }
    }

    public static Statistics queryNbHelpedRecipientsByDRTL(float minDRTL, float maxDRTL) {
        HashMap<String, Object> drtlProperties = new HashMap<String, Object>(2);
        drtlProperties.put("minDRTL", minDRTL);
        drtlProperties.put("maxDRTL", maxDRTL);
        return new Statistics(queryNbHelpedRecipients + byDRTL("v.beneficiaryFamily"), drtlProperties);
    }

    public static Statistics queryNbHelpedRecipientsByMinDRTL(float minDRTL) {
        HashMap<String, Object> drtlProperties = new HashMap<String, Object>(1);
        drtlProperties.put("minDRTL", minDRTL);
        return new Statistics(queryNbHelpedRecipients + byMinDRTL("v.beneficiaryFamily"), drtlProperties);
    }

    public static Statistics queryNbHelpedRecipientsByUnknownDRTL() {
        return new Statistics(queryNbHelpedRecipients + byUnknownDRTL("v.beneficiaryFamily"));
    }
}
