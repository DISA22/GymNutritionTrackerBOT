package config;

import config.UtilPropertis.AppProperties;

public class RapidConfig {
    private static final String URL;

    private static final String API_KEY;

    static {
        URL = AppProperties.get("rapid.exercise.url");
        API_KEY = AppProperties.get("rapid.api.key");
    }

    public static String getUrl() {
        return URL;
    }

    public static String getApiKey() {
        return API_KEY;
    }
}
