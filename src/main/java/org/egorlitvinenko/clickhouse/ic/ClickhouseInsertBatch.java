package org.egorlitvinenko.clickhouse.ic;

/**
 * @author Egor Litvinenko
 */
public class ClickhouseInsertBatch {

    public static final String TAB_SEPARATED_FORMAT = "TabSeparated";
    public static final String CSV_FORMAT = "CSV";

    private final ClickhouseHttp clickhouseHttp;
    private final String insert;
    private final PreparedStream preparedStream;
    private final int batchSize;
    private final char valueSplitter;

    private int counter;

    public ClickhouseInsertBatch(ClickhouseHttp clickhouseHttp,
                                 String sql,
                                 int batchSize,
                                 PreparedStream preparedStream) {
        this(clickhouseHttp, sql, batchSize, preparedStream, TAB_SEPARATED_FORMAT);
    }

    public ClickhouseInsertBatch(ClickhouseHttp clickhouseHttp,
                                 String sql,
                                 int batchSize,
                                 PreparedStream preparedStream,
                                 String format) {
        this.clickhouseHttp = clickhouseHttp;
        this.preparedStream = preparedStream;
        this.batchSize = batchSize;
        switch (format) {
            case CSV_FORMAT:
                this.insert = sql + " FORMAT " + CSV_FORMAT;
                this.valueSplitter = PreparedStream.CSV_VALUE_SPLITTER;
                break;
            case TAB_SEPARATED_FORMAT:
            default:
                this.insert = sql + " FORMAT " + TAB_SEPARATED_FORMAT;
                this.valueSplitter = PreparedStream.TAB_VALUE_SPLITTER;
                break;
        }
    }

    public void addBatch(String[] line) throws Exception {
        for (int i = 0; i < line.length - 1; ++i) {
            preparedStream.appendValue(line[i], valueSplitter);
        }
        preparedStream.appendLastValue(line[line.length - 1]);
        ++counter;
    }

    public boolean readyToBatch() {
        return counter % batchSize == 0;
    }

    public void endBatching() throws Exception {
        if (counter > 0) {
            execute();
        }
    }

    public void execute() throws Exception {
        InsertEntity insertEntity = InsertEntity.of(preparedStream);
        clickhouseHttp.sendInsert(insertEntity, insert);
        preparedStream.clear();
        counter = 0;
    }

}
