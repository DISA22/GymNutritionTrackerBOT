package integration.dto;

public record WorkoutExerciseDto(
        String name,
        Integer sets,
        Integer reps,
        Double weight
) {
}
