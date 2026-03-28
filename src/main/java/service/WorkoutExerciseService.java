package service;

import config.TransactionSessionManager;
import domain.Exercise;
import domain.User;
import domain.Workout;
import domain.WorkoutExercise;
import integration.dto.WorkoutExerciseDto;
import repository.WorkoutExerciseRepository;

import java.time.LocalDate;


public class WorkoutExerciseService extends BaseService<WorkoutExercise, Long, WorkoutExerciseRepository> {
    private final ExerciseService exerciseService;
    private final WorkoutService workoutService;

    public WorkoutExerciseService(WorkoutExerciseRepository repository, TransactionSessionManager transactionSessionManager, ExerciseService exerciseService, WorkoutService workoutService) {
        super(repository, transactionSessionManager);
        this.exerciseService = exerciseService;
        this.workoutService = workoutService;
    }

    public void saveWithDto(User user, WorkoutExerciseDto woExDto, LocalDate date) {
        String name = woExDto.name();
        Long telegramId = user.getTelegramId();
        Exercise exercise = exerciseService.getByName(name).orElseThrow(() -> new RuntimeException("Упражнение не найдено"));

        Workout workout = workoutService.getByDate(date, telegramId).orElseThrow(() -> new RuntimeException("Тренировка не найдена"));


        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                .exercise(exercise)
                .workout(workout)
                .reps(woExDto.reps())
                .sets(woExDto.sets())
                .weight(woExDto.weight())
                .build();

        save(workoutExercise);
    }
}
