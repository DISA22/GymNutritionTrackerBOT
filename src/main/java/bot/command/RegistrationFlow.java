package bot.command;

import bot.botStateMachine.BotState;
import bot.botStateMachine.BotStateMap;
import domain.User;
import integration.dto.UserDto;
import service.UserService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static bot.botStateMachine.BotStateMap.botStateMap;

public class RegistrationFlow implements Command {

    private final Map<String, String> userDtoMap = new ConcurrentHashMap<>();
    private final UserService userService;

    public RegistrationFlow(UserService userService) {
        this.userService = userService;
    }


    @Override
    public List<String> execute(User user, String... args) {
        Long telegramId = user.getTelegramId();
        BotState currentState = botStateMap.getOrDefault(telegramId, BotState.IDLE);

        if (currentState == BotState.IDLE) {
            botStateMap.put(user.getTelegramId(), BotState.REG_WAITING_NAME);
            return List.of("Начнем заполнение анкеты. \n Введите свое имя");
        }

        if (args.length == 0) return List.of("Пожалуйста напиши свое имя");

        String text = args[0];

        if (text.equals("выход")) botStateMap.put(telegramId, BotState.IDLE);

        switch (currentState) {
            case REG_WAITING_NAME -> {
                userDtoMap.put(telegramId + "_name", text);
                botStateMap.put(telegramId, BotState.REG_WAITING_AGE);
                return List.of("Записано. Напиши свой возраст");
            }

            case REG_WAITING_AGE -> {
                userDtoMap.put(telegramId + "_age", text);
                botStateMap.put(telegramId, BotState.REG_WAITING_HEIGHT);
                return List.of("Записано. Напиши свой рост");
            }

            case REG_WAITING_HEIGHT -> {
                userDtoMap.put(telegramId + "_height", text);
                botStateMap.put(telegramId, BotState.REG_WAITING_WEIGHT);
                return List.of("Записано. Напиши свой вес");
            }

            case REG_WAITING_WEIGHT -> {
                userDtoMap.put(telegramId + "_weight", text);
                String name = userDtoMap.get(telegramId + "_name");
                Integer age = Integer.valueOf(userDtoMap.get(telegramId + "_age"));
                Double weight = Double.valueOf(userDtoMap.get(telegramId + "_weight"));
                Double height = Double.valueOf(userDtoMap.get(telegramId + "_height"));
                UserDto userDto = new UserDto(name, age, weight, height);
                userService.completeProfile(user, userDto);
                userDtoMap.clear();
                botStateMap.clear();

                StringBuilder profile = new StringBuilder("Твой профиль\n\n");
                profile.append("Имя: ").append(user.getName()).append("\n");
                profile.append("Возраст: ").append(user.getAge()).append("\n");
                profile.append("Рост: ").append(user.getHeight()).append("\n");
                profile.append("Вес: ").append(user.getWeight()).append("\n");
                profile.append("Цель: ").append(user.getNutritionGoals()).append("\n");

                return List.of(profile.toString());
            }
        }

        return List.of();
    }
}
