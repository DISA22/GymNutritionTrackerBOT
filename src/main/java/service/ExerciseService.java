package service;

import cache.CashProvider;
import config.TransactionSessionManager;
import domain.Exercise;
import integration.RapidHttpClient;
import integration.TranslateClient;
import integration.dto.ExerciseDto;
import org.hibernate.Session;
import repository.ExerciseRepository;

import java.util.List;
import java.util.Optional;

public class ExerciseService extends BaseService<Exercise, Long, ExerciseRepository> {
    private final RapidHttpClient rapidClient;
    private final TranslateClient translateClient;

    public ExerciseService(ExerciseRepository repository, TransactionSessionManager transactionSessionManager, RapidHttpClient rapidClient, TranslateClient translateClient) {
        super(repository, transactionSessionManager);
        this.rapidClient = rapidClient;
        this.translateClient = translateClient;
    }

    public List<Exercise> getByMuscleGroup(String muscleGroup) {
        return transactionSessionManager.inSession(session -> {
            return repository.findByMuscleGroup(session, muscleGroup);
        });
    }

    public List<Exercise> getOrCreateByMuscleGroup(String muscleGroup) {
        List<Exercise> exercisesFromCache = CashProvider.get(muscleGroup);

        if (exercisesFromCache != null && !exercisesFromCache.isEmpty()) return exercisesFromCache;

        List<Exercise> exercisesFromDb = getByMuscleGroup(muscleGroup);

        if (!exercisesFromDb.isEmpty()) return exercisesFromDb;

        List<ExerciseDto> exerciseDtos = rapidClient.getListExercise(muscleGroup);

        List<Exercise> exercisesToSave = exerciseDtos
                .stream()
                .map(exercise -> {
                    return Exercise.builder()
                            .name(translateClient.cacheTranslateToRu(exercise.name().toLowerCase()))
                            .instructions(translateClient.translateFromEnToRu(String.join("\n", exercise.instructions())))
                            .equipment(translateClient.cacheTranslateToRu(exercise.equipment()))
                            .muscleGroup(translateClient.cacheTranslateToRu(exercise.target()))
                            .build();


                })
                .toList();

        saveAll(exercisesToSave);
        CashProvider.set(muscleGroup, exercisesToSave);

        return exercisesToSave;
    }

    public List<Exercise> getByQuery(String query) {
        return transactionSessionManager.inSession(session -> repository.findByQuery(session, query));
    }

    public Optional<Exercise> getByName(String name) {
        return transactionSessionManager.inSession(session -> repository.findByName(session, name));
    }
}
