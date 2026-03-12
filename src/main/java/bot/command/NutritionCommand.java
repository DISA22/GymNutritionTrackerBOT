package bot.command;

import integration.EdamanHttpClient;
import integration.dto.NutritionData;
import lombok.RequiredArgsConstructor;
import service.FoodService;

import java.util.List;

@RequiredArgsConstructor
public class NutritionCommand implements Command {
    private final FoodService foodService;

    @Override
    public String execute(String... args) {
        if (args.length == 0) return "Напиши в формате /getNutrition apple. Программа выдает отчет на 100гр продукта";

        String query = String.join(" ", args);

        var food = foodService.getOrCreateByName(query);


        StringBuilder response = new StringBuilder("Твой отчет на 100г продукта: \n");

        response.append("Продукт: ").append(food.getFoodName()).append("\n");
        response.append("Каллории: ").append(String.format("%.1f", food.getCaloriesCal())).append(" ккал").append("\n");
        response.append("Белки: ").append(String.format("%.1f", food.getProteinG())).append(" г").append("\n");
        response.append("Жиры: ").append(String.format("%.1f", food.getFatG())).append(" г").append("\n");
        response.append("Углеводы: ").append(String.format("%.1f", food.getCarbohydratesG())).append(" г").append("\n");

        return response.toString();

    }
}
