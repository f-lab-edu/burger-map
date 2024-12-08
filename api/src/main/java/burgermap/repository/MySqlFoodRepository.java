package burgermap.repository;

import burgermap.dto.food.FoodFilter;
import burgermap.entity.Food;
import burgermap.entity.QFood;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public class MySqlFoodRepository implements FoodRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public MySqlFoodRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

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

    @Override
    public void delete(Food food) {
        em.remove(food);
    }

    /**
     * querydsl을 사용하여 음식 필터링
     */
    @Override
    public List<Food> filterFood(FoodFilter foodFilter) {
        Long menuCategoryId = foodFilter.getMenuCategoryId();
        List<Long> ingredientIds = foodFilter.getIngredientIds();

        QFood food = QFood.food;
        BooleanBuilder builder = new BooleanBuilder();

        // 카테고리 필터링 - 버거, 감자튀김, 하이볼, ...
        if (menuCategoryId != null) {
            builder.and(food.menuCategory.menuCategoryId.eq(menuCategoryId));
        }
        // 재료 필터링 - 소고기 패티, 브리오슈 번, ...
        // 필터에 제시된 모든 재료를 포함하는 음식 검색
        if (ingredientIds != null) {
            ingredientIds.forEach(ingredientId ->
                    builder.and(food.ingredients.any().ingredientId.eq(ingredientId)));
        }

        return query.select(food).from(food)
                .where(builder)
                .fetch();
    }
}
