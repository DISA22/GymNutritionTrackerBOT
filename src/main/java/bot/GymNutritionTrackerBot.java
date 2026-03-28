package bot;

import bot.botStateMachine.BotState;
import bot.botStateMachine.BotStateMap;
import bot.command.WorkoutFlow;
import bot.command.RegistrationFlow;
import config.TelegramConfig;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import service.*;

import java.util.List;

@Slf4j
public class GymNutritionTrackerBot extends TelegramLongPollingBot {
    private final UserService userService;
    private final CommandResolver commandResolver;
    private final RegistrationFlow registrationFlow;
    private final WorkoutFlow workoutCommand;


    public GymNutritionTrackerBot(UserService userService, FoodService foodService, UserFoodService userFoodService, NutritionGoalsService nutritionGoalsService, ExerciseService exerciseService, RegistrationFlow registrationFlow, WorkoutFlow workoutCommand) {
        super(TelegramConfig.getBotToken());
        this.userService = userService;
        this.registrationFlow = registrationFlow;
        this.workoutCommand = workoutCommand;
        this.commandResolver = new CommandResolver(foodService, nutritionGoalsService, userFoodService, userService, exerciseService, registrationFlow, workoutCommand);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText() || update.getMessage().getFrom() == null) {
            return;
        }

        Message message = update.getMessage();

        handleMessage(message);
    }

    @Override
    public String getBotUsername() {
        return TelegramConfig.getBotUsername();
    }

    private void handleMessage(Message message) {
        Long telegramgId = message.getChatId();
        String text = message.getText();
        User from = message.getFrom();
        domain.User user = userService.getOrCreateByTelegramId(from.getUserName(), from.getId());

        try {

            BotState currentState = BotStateMap.getBotStateOrDefault(telegramgId, BotState.IDLE);

            if (currentState != BotState.IDLE) {
                handleFlow(user, text, currentState);

                return;
            }

            CommandResolver.ResolvedCommand resolvedCommand = commandResolver.resolve(telegramgId, text);
            List<String> results = resolvedCommand.command().execute(user, resolvedCommand.args());

            for (String result : results) {
                sendMessage(telegramgId, result);
            }
        } catch (CommandNotFoundException e) {
            sendMessage(telegramgId, e.getMessage());
        } catch (Exception e) {
            log.error("Failed to process message. chatId={}", telegramgId, e);
            sendMessage(telegramgId, "Произошла ошибка при обработке команды");
        }
    }

    private void sendMessage(Long chatId, String text) {
        try {
            execute(buildSendMessage(chatId, text));

        } catch (Exception e) {
            log.error("Failed to send message. chatId ={}", chatId, e);
        }
    }

    private SendMessage buildSendMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    public void handleFlow(domain.User user, String text, BotState currentState) {
        String stateName = currentState.name();

        int index = stateName.indexOf("_");

        if (index != -1) {
            String prefix = stateName.substring(0, index);
            switch (prefix) {
                case "REG" -> {
                    List<String> results = registrationFlow.execute(user, text);

                    for (String result : results) sendMessage(user.getTelegramId(), result);
                }

                case "WORKOUT" -> {
                    List<String> results = workoutCommand.execute(user, text);

                    for (String result : results) sendMessage(user.getTelegramId(), result);
                }
            }
        }

    }
}
