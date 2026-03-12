package bot;

import config.TelegramConfig;
import integration.EdamanHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import service.FoodService;
import service.UserService;

@Slf4j
public class GymNutritionTrackerBot extends TelegramLongPollingBot {
    private final UserService userService;
    private final CommandResolver commandResolver;


    public GymNutritionTrackerBot(UserService userService, FoodService foodService) {
        super(TelegramConfig.getBotToken());
        this.userService = userService;
        this.commandResolver = new CommandResolver(foodService);
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
        userService.getOrCreateByTelegramId(from.getUserName(), from.getId());

        try {
            CommandResolver.ResolvedCommand resolvedCommand = commandResolver.resolve(text);
            String result = resolvedCommand.command().execute(resolvedCommand.args());


            sendMessage(telegramgId, result);
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
}
