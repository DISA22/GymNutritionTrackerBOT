package config;

import config.UtilPropertis.AppProperties;

public class HibernateConfig {
    private static final String DIALECT;
    private static final String HBM2DDL_AUTO;
    private static final boolean SHOW_SQL;
    private static final boolean FORMAT_SQL;
    private static final String PROVIDER_CLASS;

    static {
        AppProperties properties = new AppProperties();

        DIALECT = properties.get("hibernate.dialect");
        HBM2DDL_AUTO = properties.get("hibernate.hbm2ddl.auto");
        SHOW_SQL = Boolean.parseBoolean(properties.get("hibernate.show_sql"));
        FORMAT_SQL = Boolean.parseBoolean(properties.get("hibernate.format_sql"));
        PROVIDER_CLASS = properties.get("hibernate.connection.provider_class");
    }

    public static String getDialect() {
        return DIALECT;
    }

    public static String getHbm2ddlAuto() {
        return HBM2DDL_AUTO;
    }

    public static boolean isShowSql() {
        return SHOW_SQL;
    }

    public static boolean isFormatSql() {
        return FORMAT_SQL;
    }

    public static String getProviderClass() {
        return PROVIDER_CLASS;
    }
}
