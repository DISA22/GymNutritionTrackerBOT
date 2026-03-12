package repository;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

@Slf4j

public abstract class BaseRepository<T, ID> {
    private final Class<T> entityClass;

    protected BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void save(Session session, T entity) {
        try {
            session.persist(entity);

        } catch (Exception e) {
            log.error(e.getMessage(), e);

        }
    }

    public void update(Session session, T entity) {
        try {
            session.beginTransaction();
            T t = session.merge(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void delete(Session session, T entity) {
        try {
            session.beginTransaction();
            session.remove(entity);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            System.out.println("Удалить из БД не удалось: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Optional<T> findById(Session session, ID id) {
        var entity = session.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

    public List<T> findAll(Session session) {
        String hq = "from " + entityClass.getSimpleName();
        return session.createQuery(hq, entityClass).list();
    }
}
