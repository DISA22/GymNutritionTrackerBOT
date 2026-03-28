package bot.command;

import domain.Exercise;
import domain.User;
import service.ExerciseService;

import java.util.ArrayList;
import java.util.List;

public class ExerciseCommand implements Command {
    private final ExerciseService exerciseService;

    public ExerciseCommand(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @Override
    public List<String> execute(User user, String... args) {
        if (args.length == 0) return List.of("""
                напиши в формате /getExercise biceps или бицепс. Программа выдает список упражнений на указанную группу мышц.
                Возможно в первый раз указать группу мышц надо будет на английском
                """);

        String query = String.join(" ", args);

        List<Exercise> exercises = exerciseService.getOrCreateByMuscleGroup(query);

        List<String> results = new ArrayList<>();

        for (Exercise e : exercises) {
            String formatted = String.format("%s\n Инструкция к выполнению: %s\n", e.getName(), e.getInstructions());

            results.add(formatted);
        }

        return results;
    }
}
