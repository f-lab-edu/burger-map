package burgermap.repository;

import burgermap.entity.Food;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public List<Food> findByStoreId(Long storeId) {
        String jpql = "select f from Food f where f.storeId = :storeId";
        TypedQuery<Food> query = em.createQuery(jpql, Food.class);
        query.setParameter("storeId", storeId);
        return query.getResultList();
    }
}
