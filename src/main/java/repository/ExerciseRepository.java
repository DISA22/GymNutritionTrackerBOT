package repository;

import domain.Exercise;

public class ExerciseRepository extends BaseRepository<Exercise, Long> {

    public ExerciseRepository() {
        super(Exercise.class);
    }
}
