package config;

import config.UtilPropertis.AppProperties;
import lombok.Getter;



public class TelegramConfig {
    private static final String BOT_TOKEN;
    private static final String BOT_USERNAME;

    static {
        BOT_TOKEN = AppProperties.get("telegram.bot.token");
        BOT_USERNAME = AppProperties.get("telegram.bot.username");
    }

    public static String getBotToken() {
        return BOT_TOKEN;
    }

    public static String getBotUsername() {
        return BOT_USERNAME;
    }


}
