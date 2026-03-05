package config.UtilPropertis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = AppProperties.class.getResourceAsStream("/config/application.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("Файл application.properties не найден в resources!");
            }
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить properties: " + e.getMessage(), e);
        }
    }

    public String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
