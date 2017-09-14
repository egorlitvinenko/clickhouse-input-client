package org.egorlitvinenko.clickhouse.ic;

/**
 * @author Egor Litvinenko
 */
public interface PreparedStream extends AutoCloseable {

    byte[] getBytes();

    default char[] getChars() {
        return null;
    }

    void appendValue(String value);

    void appendLastValue(String value);

    void clear();

}
