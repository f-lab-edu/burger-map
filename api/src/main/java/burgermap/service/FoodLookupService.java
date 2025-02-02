package burgermap.service;

import burgermap.dto.food.FoodFilter;
import burgermap.entity.Food;
import burgermap.exception.food.FoodNotExistException;
import burgermap.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FoodLookupService {

    private final FoodRepository repository;

    private Optional<Food> findByFoodIdPrimitive(Long foodId) {
        return repository.findByFoodId(foodId);
    }

    Food findByFoodId(Long foodId) {
        return findByFoodIdPrimitive(foodId).orElseThrow(() -> new FoodNotExistException(foodId));
    }

    CompletableFuture<Optional<Food>> findByFoodIdAsync(Long foodId) {
        return CompletableFuture.completedFuture(findByFoodIdPrimitive(foodId));
    }

    List<Food> findByStoreId(Long storeId) {
        return repository.findByStoreId(storeId);
    }
}
