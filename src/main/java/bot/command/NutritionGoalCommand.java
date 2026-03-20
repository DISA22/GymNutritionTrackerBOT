package bot.command;

import lombok.RequiredArgsConstructor;
import service.NutritionGoalsService;

@RequiredArgsConstructor
public class NutritionGoalCommand implements Command {
    @Override
    public String execute(Long id,String... args) {
        return "";
    }

    private final NutritionGoalsService nutritionGoalsService;

}
