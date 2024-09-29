package burgermap.repository;

import burgermap.entity.Ingredient;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MySqlIngredientRepository implements IngredientRepository{

    private final EntityManager em;

    @Override
    public List<Ingredient> findAll() {
        return em.createQuery("select i from Ingredient i", Ingredient.class).getResultList();
    }

    @Override
    public Optional<Ingredient> findByIngredientId(Long ingredientId) {
        Ingredient ingredient = em.find(Ingredient.class, ingredientId);
        return Optional.ofNullable(ingredient);
    }
}
