package config;

import config.UtilPropertis.AppProperties;


public class DbConfig {
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        AppProperties properties = new AppProperties();
        URL = properties.get("connection.url");
        USERNAME = properties.get("connection.username");
        PASSWORD = properties.get("connection.password");
    }

    public static String getUrl() {
        return URL;
    }

    public static String getUsername() {
        return USERNAME;
    }

    public static String getPassword() {
        return PASSWORD;
    }
}

