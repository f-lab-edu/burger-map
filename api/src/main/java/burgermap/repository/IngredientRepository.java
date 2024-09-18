package burgermap.repository;

import burgermap.entity.Ingredient;

import java.util.List;

public interface IngredientRepository {
    public List<Ingredient> findAll();
}
