package service;

import config.TransactionSessionManager;
import domain.Food;
import domain.User;
import domain.UserFood;
import repository.UserFoodRepository;

import java.time.LocalDate;
import java.util.List;


public class UserFoodService extends BaseService<UserFood, Long, UserFoodRepository> {
    private final FoodService foodService;

    public UserFoodService(UserFoodRepository repository, TransactionSessionManager transactionSessionManager, FoodService foodService) {
        super(repository, transactionSessionManager);
        this.foodService = foodService;

    }

    public List<UserFood> findAllFood() {
        return findAll();
    }

    public Food save(User user, String foodName, Double amount) {

        var food = foodService.getOrCreateByName(foodName);

        UserFood userFood = UserFood.builder()
                .user(user)
                .food(food)
                .amount(amount)
                .build();

        save(userFood);

        return food;

        /*
        var user = userService.findByTelegramId(telegramId);
        transactionSessionManager.inTx(session -> {
            UserFood userFood = UserFood.builder()
                    .user(user.get())
                    .food(food.get())
                    .date(LocalDate.from(food.get().getDateTime()))
                    .build();

            userFoodRepository.save(session, userFood);
        });*/

    }

    public String getNutrientsWithAmount(User user, String productName, double amount) {
        var food = save(user, productName, amount);

        double multiplier = amount / 100;

        StringBuilder response = new StringBuilder();
        response.append("Отчет о приеме пищи:\n");
        response.append(String.format("Продукт: %s, Масса: %.1f", food.getFoodName(), amount)).append("\n");
        response.append(String.format("Каллории: %.1f", food.getCaloriesCal() * multiplier)).append("\n");
        response.append(String.format("Белки: %.1f", food.getProteinG() * multiplier)).append("\n");
        response.append(String.format("Жиры: %.1f", food.getFatG() * multiplier)).append("\n");
        response.append(String.format("Углеводы: %.1f", food.getCarbohydratesG() * multiplier)).append("\n");

        return response.toString();
    }


    public String getFoodHistory(Long telegramId, LocalDate date) {
        List<UserFood> userFoods = transactionSessionManager.inSession(session -> {
            return repository.findAllByTelegramId(session, telegramId, date);
        });

        return analyzeNutrients(userFoods, date);

    }

    private String analyzeNutrients(List<UserFood> userFoods, LocalDate date) {
        double caloriesPerDay = userFoods.stream().mapToDouble(food -> food.getFood().getCaloriesCal() * food.getAmount() / 100).sum();
        double proteinPerDay = userFoods.stream().mapToDouble(food -> food.getFood().getProteinG() * food.getAmount() / 100).sum();
        double fatPerDay = userFoods.stream().mapToDouble(food -> food.getFood().getFatG() * food.getAmount() / 100).sum();
        double carbsPerDay = userFoods.stream().mapToDouble(food -> food.getFood().getCarbohydratesG() * food.getAmount() / 100).sum();

        StringBuilder result = new StringBuilder("Подсчет КБЖУ за " + date + " \n\n");

        result.append("Калории: ").append(caloriesPerDay).append(" Ккал\n");
        result.append("Белки: ").append(proteinPerDay).append(" г\n");
        result.append("Жиры: ").append(fatPerDay).append(" г\n");
        result.append("Углеводы: ").append(carbsPerDay).append(" г\n");

        return result.toString();

    }

}
