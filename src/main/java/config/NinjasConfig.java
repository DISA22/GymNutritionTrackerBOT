package config;

import config.UtilPropertis.AppProperties;

public class NinjasConfig {
    private static final String NINJAS_BASE_URL;
    private static final String NINJAS_EXERCISES_URL;
    private static final String NINJAS_NUTRITION_URL;
    private static final String NINJAS_API_KEY;

    static {
        NINJAS_BASE_URL = AppProperties.get("ninjas.base.url");
        NINJAS_EXERCISES_URL = AppProperties.get("ninjas.exercises.url");
        NINJAS_NUTRITION_URL = AppProperties.get("ninjas.nutrition.url");
        NINJAS_API_KEY = AppProperties.get("ninjas.api.key");
    }

    public static String getNinjasBaseUrl() {
        return NINJAS_BASE_URL;
    }

    public static String getNinjasExercisesUrl() {
        return NINJAS_EXERCISES_URL;
    }

    public static String getNinjasNutritionUrl() {
        return NINJAS_NUTRITION_URL;
    }

    public static String getNinjasApiKey() {
        return NINJAS_API_KEY;
    }
}
