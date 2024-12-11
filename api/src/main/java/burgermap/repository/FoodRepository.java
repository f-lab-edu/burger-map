package burgermap.repository;

import burgermap.dto.food.FoodFilter;
import burgermap.entity.Food;

import java.util.List;
import java.util.Optional;

public interface FoodRepository {
    public Food save(Food food);

    public Optional<Food> findByFoodId(Long foodId);

    public List<Food> findByStoreId(Long storeId);

    public Optional<Food> deleteByFoodId(Long foodId);

    public void delete(Food food);

    public List<Food> filterFood(FoodFilter foodFilter);
}
