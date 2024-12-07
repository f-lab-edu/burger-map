package burgermap.repository;

import burgermap.entity.Food;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MySqlFoodRepository implements FoodRepository{

    private final EntityManager em;

    @Override
    public Food save(Food food) {
        em.persist(food);
        return food;
    }

    @Override
    public Optional<Food> findByFoodId(Long foodId) {
        Food food = em.find(Food.class, foodId);
        return Optional.ofNullable(food);
    }

    @Override
    public List<Food> findByStoreId(Long storeId) {
        String jpql = "select f from Food f where f.store.storeId = :storeId";
        TypedQuery<Food> query = em.createQuery(jpql, Food.class);
        query.setParameter("storeId", storeId);
        return query.getResultList();
    }

    @Override
    public Optional<Food> deleteByFoodId(Long foodId) {
        Food food = em.find(Food.class, foodId);
        em.remove(food);
        return Optional.ofNullable(food);
    }
}
