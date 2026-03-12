package repository;

import domain.Food;
import org.hibernate.Session;

import java.util.Optional;

public class FoodRepository extends BaseRepository<Food, Long> {

    public FoodRepository() {
        super(Food.class);
    }

    public Optional<Food> getByName(Session session, String name) {
        return session.createQuery("from Food f where f.foodName = :name", Food.class)
                .setParameter("name", name)
                .uniqueResultOptional();
    }
}