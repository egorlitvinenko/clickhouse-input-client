package org.egorlitvinenko.clickhouse.ic;

/**
 * @author Egor Litvinenko
 */
public class BufferPreparedStream implements PreparedStream {

    private final char[] buffer;

    private int position = 0;

    public BufferPreparedStream(int capacity) {
        this.buffer = new char[capacity];
    }

    @Override
    public void appendLastValue(String value) {
        value.getChars(0, value.length(), buffer, position);
        position += value.length();
        buffer[position++] = PreparedStream.LINE_END;
    }

    @Override
    public void appendValue(String value, char valueSplitter) {
        value.getChars(0, value.length(), buffer, position);
        position += value.length();
        buffer[position++] = valueSplitter;
    }

    @Override
    public byte[] getBytes() {
        byte[] result = new byte[position];
        for (int i = 0; i < result.length; ++i) {
            result[i] = (byte) this.buffer[i];
        }
        return result;
    }

    @Override
    public void clear() {
        position = 0;
    }

    @Override
    public void close() throws Exception {

    }
}
