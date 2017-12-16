package org.egorlitvinenko.clickhouse.ic;

/**
 * @author Egor Litvinenko
 */
public interface PreparedStream extends AutoCloseable {

    char LINE_END = '\n';
    char TAB_VALUE_SPLITTER = '\t';
    char CSV_VALUE_SPLITTER = ',';

    byte[] getBytes();

    default char[] getChars() {
        return null;
    }

    default void appendValue(String value) {
        appendValue(value, TAB_VALUE_SPLITTER);
    }

    void appendValue(String value, char valueSplitter);

    void appendLastValue(String value);

    void clear();

}
