package burgermap.entity;

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
public class Food {
    private Long foodId;
    private Long storeId;
    private String name;
    private Integer price;
    private String description;
    private MenuType menuType;
    private MenuCategory menuCategory;
    private List<Ingredient> ingredients;
}
