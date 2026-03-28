import bot.GymNutritionTrackerBot;
import bot.command.WorkoutFlow;
import bot.command.RegistrationFlow;
import cache.CashProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.HibernateFactory;
import config.ObjectMapperConfiguration;
import config.TransactionSessionManager;
import integration.EdamanHttpClient;
import integration.RapidHttpClient;
import integration.TranslateClient;
import org.hibernate.SessionFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import repository.*;
import service.*;

import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = createSessionFactory();
        registerShutDownHook(sessionFactory);

        CashProvider.startCleanUpTask();


        ObjectMapper objectMapper = createMapper();

        TranslateClient translateClient = createTranslateClient(objectMapper);

        EdamanHttpClient edamanHttpClient = createEdamanClient(objectMapper, translateClient);

        RapidHttpClient rapidHttpClient = createRapidClient(translateClient, objectMapper);

        TransactionSessionManager txManager = createTxManager(sessionFactory);

        UserService userService = createUserService(txManager);

        FoodService foodService = createFoodService(txManager, edamanHttpClient, translateClient);

        UserFoodService userFoodService = createUserFoodService(foodService, txManager);

        ExerciseService exerciseService = createExerciseService(txManager, rapidHttpClient, translateClient);

        NutritionGoalsService nutritionGoalsService = createNutritionGoalsService(userFoodService, userService, txManager);

        WorkoutService workoutService = createWorkoutService(txManager);

        WorkoutExerciseService workoutExerciseService = createWorkoutExerciseService(txManager, exerciseService, workoutService);

        RegistrationFlow registrationFlow = createRegistrationFlow(userService);

        WorkoutFlow workoutFlow = createWorkoutFlow(exerciseService, workoutService, workoutExerciseService);

        GymNutritionTrackerBot gymNutritionTrackerBot = createBot(userService, foodService, userFoodService, nutritionGoalsService, exerciseService, registrationFlow, workoutFlow);

        startBot(gymNutritionTrackerBot);
    }

    private static RegistrationFlow createRegistrationFlow(UserService userService) {
        return new RegistrationFlow(userService);
    }

    private static WorkoutFlow createWorkoutFlow(ExerciseService exerciseService, WorkoutService workoutService, WorkoutExerciseService workoutExerciseService) {
        return new WorkoutFlow(exerciseService, workoutService, workoutExerciseService);
    }

    private static WorkoutExerciseService createWorkoutExerciseService(TransactionSessionManager txManager, ExerciseService exerciseService, WorkoutService workoutService) {
        WorkoutExerciseRepository workoutExerciseRepository = new WorkoutExerciseRepository();

        return new WorkoutExerciseService(workoutExerciseRepository, txManager, exerciseService, workoutService);
    }

    private static WorkoutService createWorkoutService(TransactionSessionManager txManager) {
        WorkoutRepository workoutRepository = new WorkoutRepository();

        return new WorkoutService(workoutRepository, txManager);
    }

    private static ObjectMapper createMapper() {
        return ObjectMapperConfiguration.initJackson();
    }

    private static TranslateClient createTranslateClient(ObjectMapper objectMapper) {
        return new TranslateClient(objectMapper);
    }

    private static RapidHttpClient createRapidClient(TranslateClient translateClient, ObjectMapper objectMapper) {
        return new RapidHttpClient(translateClient, objectMapper);
    }

    private static UserFoodService createUserFoodService(FoodService foodService, TransactionSessionManager txManager) {
        UserFoodRepository foodRepository = new UserFoodRepository();

        return new UserFoodService(foodRepository, txManager, foodService);
    }

    private static NutritionGoalsService createNutritionGoalsService(UserFoodService userFoodService, UserService userService, TransactionSessionManager txManager) {
        NutritionGoalsRepository nutritionGoalsRepository = new NutritionGoalsRepository();

        return new NutritionGoalsService(nutritionGoalsRepository, txManager, userFoodService, userService);
    }

    private static TransactionSessionManager createTxManager(SessionFactory sessionFactory) {

        return new TransactionSessionManager(sessionFactory);
    }


    private static SessionFactory createSessionFactory() {
        return HibernateFactory.getSessionFactory();
    }

    private static void registerShutDownHook(SessionFactory sessionFactory) {
        Runtime.getRuntime().addShutdownHook(new Thread(sessionFactory::close));
    }

    private static EdamanHttpClient createEdamanClient(ObjectMapper objectMapper, TranslateClient translateClient) {

        return new EdamanHttpClient(objectMapper, translateClient);
    }

    private static ExerciseService createExerciseService(TransactionSessionManager txManager, RapidHttpClient rapidHttpClient, TranslateClient translateClient) {
        ExerciseRepository repository = new ExerciseRepository();

        return new ExerciseService(repository, txManager, rapidHttpClient, translateClient);
    }

    private static UserService createUserService(TransactionSessionManager txManager) {
        UserRepository userRepository = new UserRepository();

        return new UserService(userRepository, txManager);
    }

    private static FoodService createFoodService(TransactionSessionManager txManager, EdamanHttpClient edamanClient, TranslateClient translateClient) {
        FoodRepository foodRepository = new FoodRepository();

        return new FoodService(foodRepository, txManager, edamanClient, translateClient);
    }

    private static GymNutritionTrackerBot createBot(UserService userService, FoodService foodService, UserFoodService userFoodService, NutritionGoalsService nutritionGoalsService, ExerciseService exerciseService, RegistrationFlow registrationFlow, WorkoutFlow workoutFlow) {
        return new GymNutritionTrackerBot(userService, foodService, userFoodService, nutritionGoalsService, exerciseService, registrationFlow, workoutFlow);
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
