package repository;

import domain.NutritionGoals;

public class NutritionGoalsRepository extends BaseRepository<NutritionGoals, Long> {
    public NutritionGoalsRepository() {
        super(NutritionGoals.class);
    }


}
