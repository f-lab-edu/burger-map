package burgermap.service;

import burgermap.entity.Food;
import burgermap.exception.food.FoodNotExistException;
import burgermap.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodLookupService {

    private final FoodRepository repository;

    Food findByFoodId(Long foodId) {
        return repository.findByFoodId(foodId).orElseThrow(() -> new FoodNotExistException(foodId));
    }
}
