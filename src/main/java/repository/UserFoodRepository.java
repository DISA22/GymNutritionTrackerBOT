package repository;

import domain.UserFood;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserFoodRepository extends BaseRepository<UserFood, Long> {

    public UserFoodRepository() {
        super(UserFood.class);
    }

    public List<UserFood> findAllByTelegramId(Session session, Long telegramId, LocalDate date) {
        String hql = """
                from UserFood uf
                join fetch uf.food
                where uf.user.telegramId = :telegramId
                and uf.date = :date
                """;

        return session.createQuery(
                        hql,
                        UserFood.class)
                .setParameter("telegramId", telegramId)
                .setParameter("date", date)
                .list();
    }
}
