package burgermap.repository;

import burgermap.entity.Food;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

//@Repository
public class HashMapFoodRepository implements FoodRepository{
    private static final Map<Long, Food> repository = new ConcurrentHashMap<>();
    private final AtomicLong foodIdCount = new AtomicLong(0);

    @Override
    public Food save(Food food) {
        food.setFoodId(foodIdCount.incrementAndGet());
        repository.put(food.getFoodId(), food);
        return food;
    }

    @Override
    public List<Food> findByStoreId(Long storeId) {
        return repository.values().stream()
                .filter(food -> food.getStore().getStoreId().equals(storeId))
                .toList();
    }
}
