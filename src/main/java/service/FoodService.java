package service;

import cache.CashProvider;
import config.TransactionSessionManager;
import domain.Food;
import integration.EdamanHttpClient;
import integration.TranslateClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import repository.FoodRepository;

import java.util.Optional;

@Slf4j
public class FoodService extends BaseService<Food, Long, FoodRepository> {
    private final EdamanHttpClient edamanClient;
    private final TranslateClient translateClient;

    public FoodService(FoodRepository repository, TransactionSessionManager transactionSessionManager, EdamanHttpClient edamanClient, TranslateClient translateClient) {
        super(repository, transactionSessionManager);
        this.edamanClient = edamanClient;
        this.translateClient = translateClient;
    }

    public Optional<Food> findByName(String name) {
        return transactionSessionManager.inSession(session -> {
            return repository.getByName(session, name);
        });
    }

    public Food getOrCreateByName(String name) {
        Food foodFromCache = CashProvider.get(name);
        if (foodFromCache != null) {
            return  foodFromCache;
        }

        return findByName(name).orElseGet(() -> {
            var foodFromEdaman = edamanClient.getNutrition(name).getFirst();

            if (foodFromEdaman == null) {
                log.error("Food not found: {}", name);
            }


            Food food = Food.builder()
                    .foodName(translateClient.translateFromEnToRu(foodFromEdaman.productName().toLowerCase()))
                    .caloriesCal(foodFromEdaman.energyKcal())
                    .proteinG(foodFromEdaman.proteins())
                    .fatG(foodFromEdaman.fat())
                    .carbohydratesG(foodFromEdaman.carbohydrates())
                    .build();

            CashProvider.set(name, food);
            save(food);

            return food;
        });

        /*if (foodFromDb.isPresent())
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

        CashProvider.set(name, food);
        save(food);

        return food;*/ //Код сереги
    }


}
