package repository;

import domain.UserFood;
import org.hibernate.Session;

import java.util.List;

public class UserFoodRepository extends BaseRepository<UserFood, Long> {

    public UserFoodRepository() {
        super(UserFood.class);
    }

    public List<UserFood> findAllByTelegramId(Session session, Long telegramId) {
        return session.createQuery(
                        "FROM UserFood uf WHERE uf.user.telegramId = :telegramId",
                        UserFood.class)
                .setParameter("telegramId", telegramId)
                .list();
    }
}
