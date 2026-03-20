package bot.command;

import lombok.RequiredArgsConstructor;
import service.NutritionGoalsService;
import service.UserFoodService;

@RequiredArgsConstructor
public class HistoryCommand implements Command {
    private final UserFoodService userFoodService;
    private final NutritionGoalsService nutritionGoalsService;

    @Override
    public String execute(Long id, String... args) {
        String info = userFoodService.getFoodHistory(id);
        String calories = nutritionGoalsService.calculateRemainingCalories(id);

        String caloriesMessage = (calories == null || calories.isEmpty())
                ? "Цель не установлена"
                : calories;

        return info + "\n" + caloriesMessage;
    }
}
