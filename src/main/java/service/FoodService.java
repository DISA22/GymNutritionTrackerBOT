package service;

import config.TransactionSessionManager;
import domain.Food;
import integration.EdamanHttpClient;
import lombok.extern.slf4j.Slf4j;
import repository.FoodRepository;

import java.util.Optional;

@Slf4j
public class FoodService extends BaseService<Food, Long, FoodRepository> {
    private final EdamanHttpClient edamanClient;

    public FoodService(FoodRepository repository, TransactionSessionManager transactionSessionManager, EdamanHttpClient edamanClient) {
        super(repository, transactionSessionManager);
        this.edamanClient = edamanClient;
    }

    public Optional<Food> findByName(String name) {
        return transactionSessionManager.inSession(session -> {
            return repository.getByName(session, name);
        });
    }

    public Food getOrCreateByName(String name) {
        var foodFromDb = findByName(name);

        if (foodFromDb.isPresent())
            return foodFromDb.get();

        var foodFromEdaman = edamanClient.getNutrition(name).getFirst();

        if (foodFromEdaman == null) {
            log.error("Food not found: {}", name);
        }


        Food food = Food.builder()
                .foodName(foodFromEdaman.productName().toLowerCase())
                .caloriesCal(foodFromEdaman.energyKcal())
                .proteinG(foodFromEdaman.proteins())
                .fatG(foodFromEdaman.fat())
                .carbohydratesG(foodFromEdaman.carbohydrates())
                .build();

        save(food);

        return food;
    }




}
