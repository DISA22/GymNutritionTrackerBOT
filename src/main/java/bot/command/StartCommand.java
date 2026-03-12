package bot.command;

public class StartCommand implements Command {
    @Override
    public String execute(String... args) {
        return "Привет! Я бот для слежения за твоими питанием и тренировками\n" +
                "Доступные команды: /start, /getNutrition";
    }
}


