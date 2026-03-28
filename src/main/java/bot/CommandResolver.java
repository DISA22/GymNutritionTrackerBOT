package bot;

import bot.command.*;
import service.*;

import java.util.Arrays;
import java.util.Map;

public class CommandResolver {
    private final Map<String, Command> commands;

    public CommandResolver(FoodService foodService, NutritionGoalsService nutritionGoalsService, UserFoodService userFoodService, UserService userService, ExerciseService exerciseService, RegistrationFlow registrationFlow, WorkoutFlow createWorkoutCommand) {
        this.commands = Map.of(
                "/start", new StartCommand(),
                "/getExercise", new ExerciseCommand(exerciseService),
                "/getNutrition", new NutritionCommand(foodService,userFoodService),
                "/getHistory", new HistoryCommand(userFoodService,nutritionGoalsService),
                "/setGoal", new NutritionGoalCommand(nutritionGoalsService,userService),
                "/trackeat", new EatTrackingCommand(userFoodService),
                "/profile", registrationFlow,
                "/createWorkout", createWorkoutCommand
        );
    }

    public ResolvedCommand resolve(Long id, String message) {
        String parcedMessage = message.trim();
        String[] parts = parcedMessage.split("\\s+");
        String commandName = normalizeCommandName(parts[0]);

        var commandHandler = commands.get(commandName);

        if (commandHandler == null) throw new CommandNotFoundException();

        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        return new ResolvedCommand(commandHandler, id, args);
    }

    private String normalizeCommandName(String rawCommand) {
        int mentionIndex = rawCommand.indexOf("@");

        if (mentionIndex <= 0) return rawCommand;

        return rawCommand.substring(0, mentionIndex);
    }

    public record ResolvedCommand(Command command, Long id, String[] args) {

    }
}
