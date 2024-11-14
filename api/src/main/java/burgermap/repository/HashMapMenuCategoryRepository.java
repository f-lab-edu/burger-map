package burgermap.repository;

import burgermap.entity.MenuCategory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

//@Repository
@Slf4j
public class HashMapMenuCategoryRepository implements MenuCategoryRepository {
    private static final Map<Long, MenuCategory> repository = new ConcurrentHashMap<>();
    private final AtomicLong menuIdCount = new AtomicLong(0);
    
    @PostConstruct
    public void init() {
        log.debug("init MenuCategory repository");
        MenuCategory burger = new MenuCategory();
        burger.setMenuCategoryId(menuIdCount.incrementAndGet());
        burger.setName("버거");
        repository.put(burger.getMenuCategoryId(), burger);

        MenuCategory frenchFries = new MenuCategory();
        frenchFries.setMenuCategoryId(menuIdCount.incrementAndGet());
        frenchFries.setName("감자 튀김");
        repository.put(frenchFries.getMenuCategoryId(), frenchFries);

        MenuCategory beer = new MenuCategory();
        beer.setMenuCategoryId(menuIdCount.incrementAndGet());
        beer.setName("맥주");
        repository.put(beer.getMenuCategoryId(), beer);
    }

    @Override
    public List<MenuCategory> findAll() {
        return repository.values().stream().toList();
    }

    @Override
    public Optional<MenuCategory> findByMenuCategoryId(Long menuCategoryId) {
        return Optional.ofNullable(repository.get(menuCategoryId));
    }
}
