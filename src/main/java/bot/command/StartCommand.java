package bot.command;

import domain.User;

import java.util.List;

public class StartCommand implements Command {

    @Override
    public List<String> execute(User user, String... args) {
        return List.of("Привет! Я бот для слежения за твоими питанием и тренировками\n" +
                "Доступные команды: /start, /getNutrition, /getExercise, /getHistory, /setGoal, /trackeat, /profile");
    }
}


