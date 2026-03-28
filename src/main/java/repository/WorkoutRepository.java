package repository;

import domain.Workout;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.Optional;

public class WorkoutRepository extends BaseRepository<Workout, Long> {

    public WorkoutRepository() {
        super(Workout.class);
    }

    public Optional<Workout> findByDate(Session session, LocalDate date, Long telegramId) {
        return session.createQuery("""
                        from Workout w 
                        where w.workoutDate = :date
                        and w.user.telegramId = :telegramId
                        """, Workout.class)
                .setParameter("date", date)
                .setParameter("telegramId", telegramId)
                .uniqueResultOptional();
    }
}
