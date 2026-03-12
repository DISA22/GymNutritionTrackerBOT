package repository;

import domain.User;
import org.hibernate.Session;

import java.util.Optional;

public class UserRepository extends BaseRepository<User, Long> {

    public UserRepository() {
        super(User.class);
    }

    public Optional<User> findByTelegramId(Session session, Long telegramId) {
        return session.createQuery("from User u where u.telegramId = :telegramId", User.class)
                .setParameter("telegramId", telegramId)
                .uniqueResultOptional();
    }
}
