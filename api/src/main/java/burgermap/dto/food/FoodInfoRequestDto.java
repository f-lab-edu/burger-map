package burgermap.dto.food;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodInfoRequestDto {
    private String name;
    private Integer price;
    private String description;
    private String menuTypeValue;
    private Long menuCategoryId;
    private List<Long> ingredientIds;
}
