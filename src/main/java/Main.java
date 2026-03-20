import bot.GymNutritionTrackerBot;
import cache.CashProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.HibernateFactory;
import config.ObjectMapperConfiguration;
import config.TransactionSessionManager;
import integration.EdamanHttpClient;
import integration.NinjasHttpClient;
import org.hibernate.SessionFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import repository.FoodRepository;
import repository.NutritionGoalsRepository;
import repository.UserFoodRepository;
import repository.UserRepository;
import service.FoodService;
import service.NutritionGoalsService;
import service.UserFoodService;
import service.UserService;

import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = createSessionFactory();
        registerShutDownHook(sessionFactory);

        //NinjasHttpClient ninjasHttpClient = new NinjasHttpClient(objectMapper);

        EdamanHttpClient edamanHttpClient = createEdamanClient();

        UserService userService = createUserService(sessionFactory);

        FoodService foodService = createFoodService(sessionFactory, edamanHttpClient);

        UserFoodService userFoodService = createUserFoodService(userService,foodService,sessionFactory);

        NutritionGoalsService nutritionGoalsService = createNutritionGoalsService(sessionFactory, userFoodService, userService);

        GymNutritionTrackerBot gymNutritionTrackerBot = createBot(userService, foodService, userFoodService, nutritionGoalsService);

        startBot(gymNutritionTrackerBot);
    }

    private static UserFoodService createUserFoodService(UserService userService,FoodService foodService,SessionFactory sessionFactory){
        TransactionSessionManager transactionSessionManager = new TransactionSessionManager(sessionFactory);
        UserFoodRepository foodRepository = new UserFoodRepository();
        return new UserFoodService(userService, foodService, transactionSessionManager, foodRepository);
    }
    private static NutritionGoalsService createNutritionGoalsService(SessionFactory sessionFactory, UserFoodService userFoodService, UserService userService) {
        NutritionGoalsRepository nutritionGoalsRepository = new NutritionGoalsRepository();
        TransactionSessionManager transactionSessionManager1 = new TransactionSessionManager(sessionFactory);
        return new NutritionGoalsService(nutritionGoalsRepository, transactionSessionManager1, userFoodService, userService);
    }


    private static SessionFactory createSessionFactory() {
        return HibernateFactory.getSessionFactory();
    }

    private static void registerShutDownHook(SessionFactory sessionFactory) {
        Runtime.getRuntime().addShutdownHook(new Thread(sessionFactory::close));
    }

    private static EdamanHttpClient createEdamanClient() {
        ObjectMapper objectMapper = ObjectMapperConfiguration.initJackson();
        return new EdamanHttpClient(objectMapper);
    }

    private static UserService createUserService(SessionFactory sessionFactory) {
        UserRepository userRepository = new UserRepository();
        TransactionSessionManager txSessionManager = new TransactionSessionManager(sessionFactory);
        return new UserService(userRepository, txSessionManager);
    }

    private static FoodService createFoodService(SessionFactory sessionFactory, EdamanHttpClient client) {
        FoodRepository foodRepository = new FoodRepository();
        TransactionSessionManager txSessionManager = new TransactionSessionManager(sessionFactory);
        return new FoodService(foodRepository, txSessionManager, client);
    }

    private static GymNutritionTrackerBot createBot(UserService userService, FoodService foodService, UserFoodService userFoodService, NutritionGoalsService nutritionGoalsService) {
        return new GymNutritionTrackerBot(userService, foodService, userFoodService, nutritionGoalsService);
    }

    private static void startBot(GymNutritionTrackerBot bot) {
        try {

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            telegramBotsApi.registerBot(bot);

            new CountDownLatch(1).await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
