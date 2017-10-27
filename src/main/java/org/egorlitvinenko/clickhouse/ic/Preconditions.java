package org.egorlitvinenko.clickhouse.ic;

/**
 * @author Egor Litvinenko
 */
public class Preconditions {

    public static boolean isNullOrEmpty(String value) {
        return null == value || value.isEmpty();
    }

}
