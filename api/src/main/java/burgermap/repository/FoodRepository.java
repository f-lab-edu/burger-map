package burgermap.repository;

import burgermap.entity.Burger;
import burgermap.entity.Food;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository {
    public Food save(Food food);

    public List<Food> findByStoreId(Long storeId);
}
