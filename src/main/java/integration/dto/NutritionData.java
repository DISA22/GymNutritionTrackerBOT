package integration.dto;

public record NutritionData(
        String productName,
        double energyKcal,
        double proteins,
        double fat,
        double carbohydrates
) {
}
