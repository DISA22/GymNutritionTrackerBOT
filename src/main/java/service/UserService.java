package service;

import config.TransactionSessionManager;
import domain.User;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.UserRepository;

import java.util.Optional;

@Slf4j


public class UserService extends BaseService<User, Long, UserRepository> {


    public UserService(UserRepository repository, TransactionSessionManager transactionSessionManager) {
        super(repository, transactionSessionManager);
    }

    public Optional<User> findByTelegramId(Long telegramId) {
        return transactionSessionManager.inSession(session -> repository.findByTelegramId(session, telegramId));

    }

    public User getOrCreateByTelegramId(String username, Long telegramId ) {
        var userFromDb = findByTelegramId(telegramId);

        if (userFromDb.isPresent()) {
            return userFromDb.get();
        }

        var user = User.builder()
                .telegramId(telegramId)
                .username(username)
                .build();

        save(user);

        return user;
    }
}
