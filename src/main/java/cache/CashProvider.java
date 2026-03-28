package cache;


import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.*;

@Slf4j
public class CashProvider {
    private static final ConcurrentHashMap<String, CacheEntry> map = new ConcurrentHashMap<String, CacheEntry>();

    private static final long defaultTtlMillis = Duration.ofMinutes(5).toMillis();

    private static final long cleanUpPeriodMillis = Duration.ofSeconds(30).toMillis();

    private static final ScheduledExecutorService sheduler = Executors.newScheduledThreadPool(1);

    public static ScheduledFuture<?> startCleanUpTask() {
        return sheduler.scheduleWithFixedDelay(CashProvider::cleanUpExpiresEntries, cleanUpPeriodMillis, cleanUpPeriodMillis, TimeUnit.MILLISECONDS);
    }

    private static void cleanUpExpiresEntries() {
        for (var entry : map.entrySet()) {
            if (entry.getValue().isExpired()) map.remove(entry.getKey());
        }
    }


    public static void set(String key, Object value) {
        var cacheEntry = new CacheEntry(value, System.currentTimeMillis() + defaultTtlMillis);

        map.put(key, cacheEntry);
    }

    public static <T> T get(String key) {
        log.info("ВЫТАЩИЛИ ИЗ КЕША по ключу {}", key);
        log.info("Значение: {}", key);
        CacheEntry cacheEntry = map.get(key);
        if (cacheEntry == null || cacheEntry.isExpired()) {
            log.atWarn().addKeyValue("key", key).log("Ключ не найден в кеше");
            return null;
        }
        return (T) cacheEntry.value();
    }
}
