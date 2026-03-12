package bot;

import bot.command.Command;
import bot.command.ExerciseCommand;
import bot.command.NutritionCommand;
import bot.command.StartCommand;
import integration.EdamanHttpClient;
import service.FoodService;

import java.util.Arrays;
import java.util.Map;

public class CommandResolver {
    private final Map<String, Command> commands;

    public CommandResolver(FoodService foodService) {
        this.commands = Map.of(
                "/start", new StartCommand(),
                "/getExercise", new ExerciseCommand(),
                "/getNutrition", new NutritionCommand(foodService)
        );
    }

    public ResolvedCommand resolve(String message) {
        String parcedMessage = message.trim();
        String[] parts = parcedMessage.split("\\s+");
        String commandName = normalizeCommandName(parts[0]);

        var commandHandler = commands.get(commandName);

        if (commandHandler == null) throw new CommandNotFoundException();

        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        return new ResolvedCommand(commandHandler, args);
    }

    private String normalizeCommandName(String rawCommand) {
        int mentionIndex = rawCommand.indexOf("@");

        if (mentionIndex <= 0) return rawCommand;

        return rawCommand.substring(0, mentionIndex);
    }

    public record ResolvedCommand(Command command, String[] args) {

    }
}
