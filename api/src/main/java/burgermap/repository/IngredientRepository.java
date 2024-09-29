package burgermap.repository;

import burgermap.entity.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository {
    public List<Ingredient> findAll();

    public Optional<Ingredient> findByIngredientId(Long ingredientId);
}
