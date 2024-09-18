package burgermap.repository;

import burgermap.entity.Food;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository {
    public Food save(Food food);

    public List<Food> findByStoreId(Long storeId);
}
