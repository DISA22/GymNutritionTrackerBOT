package bot.command;

import domain.NutritionGoals;
import domain.User;
import lombok.RequiredArgsConstructor;
import service.NutritionGoalsService;
import service.UserService;

import java.util.List;

@RequiredArgsConstructor
public class NutritionGoalCommand implements Command {
    private final NutritionGoalsService nutritionGoalsService;
    private final UserService userService;

    @Override
    public List<String> execute(User user, String... args) {
        try {
            Double calories = Double.parseDouble(args[0]);
            /*var user = userService.findByTelegramId(id)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));*/

            if (user.getNutritionGoals() == null) {
                NutritionGoals nutritionGoals = NutritionGoals.builder()
                        .user(user)
                        .calories(calories)
                        .build();

                nutritionGoalsService.save(nutritionGoals);

                user.setNutritionGoals(nutritionGoals);
                return List.of("Цель установлена: " + calories + " ккал");
            } else {
                return List.of("У вас уже установлена цель: " +
                        user.getNutritionGoals().getCalories() + " ккал");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of("Во время установки цели произошла ошибка" + e.getMessage());
        }
    }
}
