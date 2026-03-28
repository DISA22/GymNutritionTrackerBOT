package bot.botStateMachine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BotStateMap {
    public static final Map<Long, BotState> botStateMap = new ConcurrentHashMap<>();

    public static BotState getBotStateOrDefault(Long telegramId, BotState state) {
       return botStateMap.getOrDefault(telegramId, state);
    }

    public static void putInMap(Long telegramId, BotState state) {
        botStateMap.put(telegramId, state);
    }
}
