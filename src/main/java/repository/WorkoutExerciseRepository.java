package repository;

import domain.WorkoutExercise;

public class WorkoutExerciseRepository extends BaseRepository<WorkoutExercise, Long> {

    public WorkoutExerciseRepository() {
        super(WorkoutExercise.class);
    }

}
