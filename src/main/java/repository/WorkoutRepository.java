package repository;

import domain.Workout;

public class WorkoutRepository extends BaseRepository<Workout, Long> {

    public WorkoutRepository() {
        super(Workout.class);
    }

}
