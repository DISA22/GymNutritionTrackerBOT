package integration.dto;

import java.util.List;

public record ExerciseDto(
        String name,
        String target, //в апи группа мышц
        String equipment,
        List<String> instructions
) {
}
