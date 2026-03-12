package config;

import config.UtilPropertis.AppProperties;

public class EdamanConfig {
    private static final String FOOD_BASE_URL;
    private static final String API_KEY;
    private static final String APPLICATION_ID;

    static {
        FOOD_BASE_URL = AppProperties.get("edaman.food.url");
        API_KEY = AppProperties.get("edaman.api.key");
        APPLICATION_ID = AppProperties.get("edaman.application.id");

    }

    public static String getFoodBaseUrl() {
        return FOOD_BASE_URL;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getApplicationId() {
        return APPLICATION_ID;
    }
}
