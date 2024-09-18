package burgermap.repository;

import burgermap.entity.Ingredient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Slf4j
public class HashMapIngredientRepository implements IngredientRepository {
    private static final Map<Long, Ingredient> repository = new ConcurrentHashMap<>();
    private final AtomicLong ingredientIdCount = new AtomicLong(0);
    
    @PostConstruct
    public void init() {
        log.debug("init ingredient repository");
        Ingredient beefPatty = new Ingredient();
        beefPatty.setIngredientId(ingredientIdCount.incrementAndGet());
        beefPatty.setName("소고기 패티");
        repository.put(beefPatty.getIngredientId(), beefPatty);

        Ingredient cheddarCheese = new Ingredient();
        cheddarCheese.setIngredientId(ingredientIdCount.incrementAndGet());
        cheddarCheese.setName("체다 치즈");
        repository.put(cheddarCheese.getIngredientId(), cheddarCheese);

        Ingredient briocheBun = new Ingredient();
        briocheBun.setIngredientId(ingredientIdCount.incrementAndGet());
        briocheBun.setName("브리오슈 번");
        repository.put(briocheBun.getIngredientId(), briocheBun);
    }

    @Override
    public List<Ingredient> findAll() {
        return repository.values().stream().toList();
    }
}
