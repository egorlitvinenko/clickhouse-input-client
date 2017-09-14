<a href="https://github.com/yandex/clickhouse-jdbc">clickhouse-jdbc</a> is a JDBC wrapper, which uses Http Client from Apache Http Components to write to <a href="https://clickhouse.yandex">Clickhouse</a>.

Http communication is externalized from clickhouse-jdbc and modified a little bit to reduce impact on GC.

It is used in my <a href="https://github.com/egorlitvinenko/testparsing">experiment</a>.
Basic usage from there:

```java
ClickhouseInsertBatch insertBatch = new ClickhouseInsertBatch(fromCreateToLoadBenchmark.clickhouseHttp,
                "INSERT INTO test.TEST_DATA_1M_9C_DATE (ID, f1, f2, f3, f4, f5, f6, f7, f8)",
                BATCH_SIZE,
                new BufferPreparedStream(BATCH_SIZE * 1000)
        );

ChLineEventHandler handler = new ChLineEventHandler(insertBatch);

public class ChLineEventHandler implements EventHandler<LineEvent> {

    private final ClickhouseInsertBatch insertBatch;

    public ChLineEventHandler(ClickhouseInsertBatch insertBatch) {
        this.insertBatch = insertBatch;
    }

    @Override
    public void onEvent(LineEvent event, long sequence, boolean endOfBatch) throws Exception {
        if (event.endStream()) {
            insertBatch.endBatching();
        } else if (event.isFinished() && event.isValid()) {
            insertBatch.addBatch(event.values());
            if (insertBatch.readyToBatch()) {
                insertBatch.execute();
            }
        }
    }

}
```
