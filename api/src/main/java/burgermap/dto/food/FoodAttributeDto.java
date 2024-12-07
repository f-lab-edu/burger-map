package burgermap.dto.food;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FoodAttributeDto {
    List<String> menuTypes;
    List<MenuCategoryInfoDto> menuCategories;
    List<IngredientInfoDto> ingredients;
}
