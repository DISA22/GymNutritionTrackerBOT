package bot.command;

import domain.User;
import domain.UserFood;
import service.UserFoodService;

import java.util.List;

public class EatTrackingCommand implements Command {
    private final UserFoodService userFoodService;

    public EatTrackingCommand(UserFoodService userFoodService) {
        this.userFoodService = userFoodService;
    }

    @Override
    public List<String> execute(User user, String... args) {
        if (args.length == 0) return List.of("Напиши в формате /trackeat chicken 200");

        String productName = args[0];
        double amount = Double.parseDouble(args[1]);

        return List.of(userFoodService.getNutrientsWithAmount(user, productName, amount));
    }
}
