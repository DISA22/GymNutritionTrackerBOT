package bot.command;

import bot.botStateMachine.BotState;
import domain.Exercise;
import domain.User;
import integration.dto.WorkoutDto;
import integration.dto.WorkoutExerciseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import service.ExerciseService;
import service.WorkoutExerciseService;
import service.WorkoutService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static bot.botStateMachine.BotStateMap.botStateMap;

@Slf4j
@RequiredArgsConstructor
public class WorkoutFlow implements Command {

    private final Map<String, Object> workoutDtoMap = new ConcurrentHashMap<>();


    private final ExerciseService exerciseService;
    private final WorkoutService workoutService;
    private final WorkoutExerciseService workoutExerciseService;


    @Override
    public List<String> execute(User user, String... args) {
        Long telegramId = user.getTelegramId();

        BotState currentState = botStateMap.getOrDefault(telegramId, BotState.IDLE);


        if (currentState == BotState.IDLE) {
            botStateMap.put(telegramId, BotState.WORKOUT_WAITING_DATE);
            return List.of("Введи дату тренировки в формате dd.mm.yyyy");
        }

        if (args.length == 0) return List.of("Пожалуйста напиши дату");

        String text = args[0];

        if (text.equals("выход")) botStateMap.put(telegramId, BotState.IDLE);

        switch (currentState) {
            case WORKOUT_WAITING_DATE -> {
                handleDate(telegramId, text);
                return List.of("Записано. Напиши тип тренировки");
            }
            case WORKOUT_WAITING_TYPE -> {
                handleType(telegramId, text);

                createAndSaveWorkoutDto(user);
                return List.of("Записано. Напиши название упражнения");
            }
            case WORKOUT_WAITING_EXERCISE_NAME -> {
                return handleExerciseName(telegramId, text);
                //Здесь мы должны пользователю выкинуть список упражнений и позволить ему выбрать вариант из списка


            }

            //Здесь какая-то логика если пользователь выбрал какое-то из упражнений

            case WORKOUT_WAITING_SETS -> {
                handleSets(telegramId, text);
                return List.of("Записано. Укажи количество повторений");
            }
            case WORKOUT_WAITING_REPS -> {
                handleReps(telegramId, text);
                return List.of("Записано. Укажи вес с которым будешь работать");
            }
            case WORKOUT_WAITING_EXERCISE_WEIGHT -> {
                handleExerciseWeight(telegramId, text);

                createWorkoutExerciseAndSaveInDb(user);
                return List.of("Записано и сохранено. Хочешь добавить еще упражнений в тренировку?");
            }

            case WORKOUT_WAITING_YES_OR_NOT -> {
                return handleAnswer(telegramId, text);
            }
        }

        return List.of("?");
    }

    public void handleDate(Long telegramId, String text) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        LocalDate date = LocalDate.parse(text, formatter);

        workoutDtoMap.put(telegramId + "_date", date);
        botStateMap.put(telegramId, BotState.WORKOUT_WAITING_TYPE);

    }

    public void handleType(Long telegramId, String text) {
        workoutDtoMap.put(telegramId + "_type", text);


        botStateMap.put(telegramId, BotState.WORKOUT_WAITING_EXERCISE_NAME);

    }

    public List<String> handleExerciseName(Long telegramId, String text) {
        List<Exercise> exerciseList = exerciseService.getByQuery(text);

        if (exerciseList == null || exerciseList.isEmpty()) return List.of("Ничего не нашлось по данному запросу");
        //Здесь мы как то должны обработать список чтоб он выдал список упражнений. На будущее
        workoutDtoMap.put(telegramId + "_exercise_name", exerciseList.getFirst().getName());
        botStateMap.put(telegramId, BotState.WORKOUT_WAITING_SETS);
        return List.of("Записано. Укажи количество подходов");
    }

    public void handleSets(Long telegramId, String text) {
        Integer intSets = Integer.parseInt(text);
        workoutDtoMap.put(telegramId + "_sets", intSets);
        botStateMap.put(telegramId, BotState.WORKOUT_WAITING_REPS);
    }

    public void handleReps(Long telegramId, String text) {
        Integer intReps = Integer.parseInt(text);
        workoutDtoMap.put(telegramId + "_reps", intReps);
        botStateMap.put(telegramId, BotState.WORKOUT_WAITING_EXERCISE_WEIGHT);
    }

    public void handleExerciseWeight(Long telegramId, String text) {
        Double weight = Double.parseDouble(text);

        workoutDtoMap.put(telegramId + "_exercise_weight", weight);



        botStateMap.put(telegramId, BotState.WORKOUT_WAITING_YES_OR_NOT);
    }

    public List<String> handleAnswer(Long telegramId, String text) {
        if (text.equals("да")) {
            botStateMap.put(telegramId, BotState.WORKOUT_WAITING_EXERCISE_NAME);
            return List.of("Хорошо. Пиши название упражнения");
        } else if (text.equals("нет")) {
            botStateMap.put(telegramId, BotState.IDLE);
            return List.of("Хорошо. Мы закончили, напиши /start");
        }
        return List.of("повтори попытку");
    }

    public void createAndSaveWorkoutDto(User user) {
        Long telegramId = user.getTelegramId();
        LocalDate date = (LocalDate) workoutDtoMap.get(telegramId + "_date");
        String type = (String) workoutDtoMap.get(telegramId + "_type");
        WorkoutDto workoutDto = new WorkoutDto(date, type);

        try {
            workoutService.saveWithDto(user, workoutDto);
        } catch (Exception e) {
            log.error("Ошибка при работе с бд: {}", e.getMessage());
        }


    }

    public void createWorkoutExerciseAndSaveInDb(User user) {
        Long telegramId = user.getTelegramId();
        String name = (String) workoutDtoMap.get(telegramId + "_exercise_name");
        LocalDate date = (LocalDate) workoutDtoMap.get(telegramId + "_date");
        Integer sets = (Integer) workoutDtoMap.get(telegramId + "_sets");
        Integer reps = (Integer) workoutDtoMap.get(telegramId + "_reps");
        Double weight = (Double) workoutDtoMap.get(telegramId + "_exercise_weight");

        WorkoutExerciseDto woExDto = new WorkoutExerciseDto(name, sets, reps, weight);

        try {
            workoutExerciseService.saveWithDto(user, woExDto, date);
        } catch (Exception e) {
            log.error("Ошибка при работе с бд: {}", e.getMessage());
        }


    }



}
