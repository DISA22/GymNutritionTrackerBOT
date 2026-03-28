package config;

import config.UtilPropertis.AppProperties;

public class TranslateConfig {
    private static final String URL;

    private static final String FROM_EN_TO_RU;

    private static final String FROM_RU_TO_EN;

    static {
        URL = AppProperties.get("google.translate.free.api");
        FROM_EN_TO_RU = AppProperties.get("google.from.en.to.ru");
        FROM_RU_TO_EN = AppProperties.get("google.from.ru.to.en");
    }

    public static String getUrl() {
        return URL;
    }

    public static String getFromRuToEn() {
        return FROM_RU_TO_EN;
    }

    public static String getFromEnToRu() {
        return FROM_EN_TO_RU;
    }
}
