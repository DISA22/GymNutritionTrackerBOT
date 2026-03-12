package repository;

import domain.UserFood;

public class UserFoodRepository extends BaseRepository<UserFood, Long> {

    public UserFoodRepository() {
        super(UserFood.class);
    }

}
