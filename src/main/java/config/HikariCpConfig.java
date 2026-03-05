package config;

import config.UtilPropertis.AppProperties;

public class HikariCpConfig {
    private static final int MAXIMUM_POOL_SIZE;
    private static final int MINIMUM_IDLE;
    private static final long CONNECTION_TIMEOUT;
    private static final String DRIVER_CLASS_NAME;

    static {
        AppProperties appProperties = new AppProperties();
        MAXIMUM_POOL_SIZE = Integer.parseInt(appProperties.get("hikari.maximumPoolSize"));
        MINIMUM_IDLE = Integer.parseInt(appProperties.get("hikari.minimumIdle"));
        CONNECTION_TIMEOUT = Long.parseLong(appProperties.get("hikari.connectionTimeout"));
        DRIVER_CLASS_NAME = appProperties.get("connection.driver_class");
    }

    public static int getMaximumPoolSize() {
        return MAXIMUM_POOL_SIZE;
    }

    public static int getMinimumIdle() {
        return MINIMUM_IDLE;
    }

    public static long getConnectionTimeout() {
        return CONNECTION_TIMEOUT;
    }

    public static String getDriverClassName() {
        return DRIVER_CLASS_NAME;
    }
}
