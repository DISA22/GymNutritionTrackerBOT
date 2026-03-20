package service;

import config.TransactionSessionManager;
import domain.Food;
import domain.UserFood;
import lombok.RequiredArgsConstructor;
import repository.UserFoodRepository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class UserFoodService {
    private final UserService userService;
    private final FoodService foodService;
    private final TransactionSessionManager transactionSessionManager;
    private final UserFoodRepository userFoodRepository;

    public void saveFood(Long telegramId, String foodName) {
        var user = userService.findByTelegramId(telegramId);
        var food = foodService.findByName(foodName);
        transactionSessionManager.inTx(session -> {
            UserFood userFood = UserFood.builder()
                    .user(user.get())
                    .food(food.get())
                    .date(LocalDate.from(food.get().getDateTime()))
                    .build();

            userFoodRepository.save(session, userFood);
        });

    }

    public String getFoodHistory(Long telegramId) {
        List<UserFood> userFoods = transactionSessionManager.inSession(session -> {
            return userFoodRepository.findAllByTelegramId(session, telegramId);
        });

        StringBuilder result = new StringBuilder("История питания:\n\n");
        for (UserFood userFood : userFoods) {
            result.append(userFood.getDate()).append("\n")
                    .append("Наименование продукта : ").append(userFood.getFood().getFoodName())
                    .append("  |  Белок : ").append(userFood.getFood().getProteinG())
                    .append(" г  |  Калории : ").append(userFood.getFood().getCaloriesCal())
                    .append(" ккал\n");
        }
        return result.toString() + allBju(userFoods);
    }

    private String allBju(List<UserFood> userFoods) {
        int calories = 0;
        int protein = 0;
        for (UserFood userFood : userFoods) {
            calories += userFood.getFood().getCaloriesCal();
            protein += userFood.getFood().getProteinG();
        }
        return "Каллории: " + calories + " ккал\n" + "Белки: " + protein + " г";
    }

    public List<UserFood> findAllFood() {
        return transactionSessionManager.inSession(session ->
                userFoodRepository.findAll(session));
    }
}
