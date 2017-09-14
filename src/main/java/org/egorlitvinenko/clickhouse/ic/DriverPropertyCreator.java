package org.egorlitvinenko.clickhouse.ic;

import java.sql.DriverPropertyInfo;
import java.util.Properties;

public interface DriverPropertyCreator {
    DriverPropertyInfo createDriverPropertyInfo(Properties properties);
}
