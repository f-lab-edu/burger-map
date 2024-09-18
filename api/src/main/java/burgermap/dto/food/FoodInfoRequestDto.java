package burgermap.dto.food;

import burgermap.entity.Ingredient;
import burgermap.entity.MenuCategory;
import burgermap.enums.MenuType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FoodInfoRequestDto {
    private String name;
    private Integer price;
    private String description;
    private MenuType menuType;
    private MenuCategory menuCategory;
    private List<Ingredient> ingredients;
}
