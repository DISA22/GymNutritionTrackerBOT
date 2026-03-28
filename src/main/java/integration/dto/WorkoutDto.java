package integration.dto;

import java.time.LocalDate;

public record WorkoutDto(
        LocalDate date,
        String type
) {
}
