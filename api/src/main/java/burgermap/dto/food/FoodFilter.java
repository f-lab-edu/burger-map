package burgermap.dto.food;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FoodFilter {
    private Long menuCategoryId;
    private List<Long> ingredientIds;
}
