package service;

import config.TransactionSessionManager;
import domain.Food;
import domain.NutritionGoals;
import domain.User;
import repository.NutritionGoalsRepository;

import java.util.List;

public class NutritionGoalsService extends BaseService<NutritionGoals, Long, NutritionGoalsRepository> {
    private final UserFoodService userFoodService;
    private final UserService userService;

    public NutritionGoalsService(NutritionGoalsRepository repository, TransactionSessionManager transactionSessionManager, UserFoodService userFoodService, UserService userService) {
        super(repository, transactionSessionManager);
        this.userFoodService = userFoodService;
        this.userService = userService;
    }

    public void setGoal(Long id, Double calories) {
        transactionSessionManager.inTx(session -> {
            User user = userService.findByIdInSameSession(session, id);

            NutritionGoals goal = NutritionGoals.builder()
                    .user(user)
                    .calories(calories)
                    .build();

            repository.save(session, goal);
        });
    }

    private List<Food> getAllFood(Long idTg) {
        User user = userService.findByTelegramId(idTg).get();
        var id = user.getId();
        String sql = """
                SELECT foods.* 
                FROM foods 
                RIGHT JOIN user_foods uf ON foods.id = uf.food_id
                WHERE uf.user_id = :userId
                """;

        List<Food> foods = transactionSessionManager.inSession(session ->
                session.createNativeQuery(sql, Food.class)
                        .setParameter("userId", id)
                        .list()
        );

        if (foods.isEmpty()) {
            throw new RuntimeException("Не смогли получить список продуктов человека");
        }
        return foods;
    }

    private Double getNutritionGoals(Long telegramId) {
        User user = userService.findByTelegramId(telegramId).get();
        var id = user.getId();
        String sql = """
                select calories from nutrition_goals 
                where user_id = :userId
                """;
        var calories = transactionSessionManager.inSession(session -> {
            return session.createNativeQuery(sql)
                    .setParameter("userId", id)
                    .uniqueResult();
        });

        return (Double) calories;
    }

    public String calculateRemainingCalories(Long TgId) {
        List<Food> foods = getAllFood(TgId);
        Double nutritionGoals = getNutritionGoals(TgId);

        if (nutritionGoals == null || nutritionGoals == 0) {
            return "Цель по калориям не установлена";
        }

        int calories = 0;
        for (Food food : foods) {
            calories += food.getCaloriesCal();
        }

        double remaining = nutritionGoals - calories;

        if (remaining < 0) {
            return "Вы превысили дневную норму на " + Math.abs(remaining) + " ккал";
        }

        return "Осталось кушать: " + remaining + " ккал";
    }
}