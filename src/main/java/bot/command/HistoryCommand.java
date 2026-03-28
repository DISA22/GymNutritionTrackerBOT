package bot.command;

import domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import service.NutritionGoalsService;
import service.UserFoodService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class HistoryCommand implements Command {
    private final UserFoodService userFoodService;
    private final NutritionGoalsService nutritionGoalsService;

    @Override
    public List<String> execute(User user, String... args) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            LocalDate date = LocalDate.parse(args[0], format);
            String info = userFoodService.getFoodHistory(user.getTelegramId(), date);
            String calories = nutritionGoalsService.calculateRemainingCalories(user.getTelegramId());

            String caloriesMessage = (calories == null || calories.isEmpty())
                    ? "Цель не установлена"
                    : calories;
            return List.of(info + "\n" + caloriesMessage);

        } catch (Exception e) {
            log.error("Parcing error: {}", e.getMessage());
            return List.of("Возможно проблема с парсингом даты. Пожалуйста напиши в формате dd.mm.yyyy");
        }
    }
}
