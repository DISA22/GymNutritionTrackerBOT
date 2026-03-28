package service;

import config.TransactionSessionManager;
import domain.User;
import domain.Workout;
import integration.dto.WorkoutDto;
import org.hibernate.Session;
import repository.WorkoutRepository;

import java.time.LocalDate;
import java.util.Optional;

public class WorkoutService extends BaseService<Workout, Long, WorkoutRepository> {
    public WorkoutService(WorkoutRepository repository, TransactionSessionManager transactionSessionManager) {
        super(repository, transactionSessionManager);
    }

    public void saveWithDto(User user, WorkoutDto workoutDto) {
        Workout workout = Workout.builder()
                .user(user)
                .workoutDate(workoutDto.date())
                .type(workoutDto.type())
                .build();

        save(workout);

    }

    public Optional<Workout> getByDate(LocalDate date, Long telegramId) {
        return transactionSessionManager.inSession(session -> repository.findByDate(session, date, telegramId));
    }


}
