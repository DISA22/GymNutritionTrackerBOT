package repository;

import domain.Exercise;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class ExerciseRepository extends BaseRepository<Exercise, Long> {

    public ExerciseRepository() {
        super(Exercise.class);
    }

    public List<Exercise> findByMuscleGroup(Session session,String muscleGroup) {
        return session.createQuery("from Exercise e where e.muscleGroup = :muscleGroup", Exercise.class)
                .setParameter("muscleGroup", muscleGroup)
                .list();
    }

    public List<Exercise> findByQuery(Session session, String query) {
        return session.createQuery("from Exercise e where lower(e.name) like lower(:query)", Exercise.class)
                .setParameter("query", "%" + query + "%")
                .list();
    }

    public Optional<Exercise> findByName(Session session, String name) {
        return session.createQuery("from Exercise e where e.name = :name", Exercise.class)
                .setParameter("name", name)
                .uniqueResultOptional();
    }
}
