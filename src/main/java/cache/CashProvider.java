package cache;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class CashProvider {
    private static final ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();

    public static void set(String key, Object value) {
        map.put(key, value);
    }

    public static <T> T get(String key) {
        log.error("ВЫТАЩИЛИ ИЗ КЕША по ключу {}", key);
        log.error("Значение: {}", key);
        Object value = map.get(key);
        if (value == null) {
            log.atWarn().addKeyValue("key", key).log("Ключ не найден в кеше");
            return null;
        }
        return (T) value;
    }
}
