package burgermap.entity;

import burgermap.enums.MenuType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;
    @ManyToOne
    private Store store;
    private String name;
    private Integer price;
    private String description;
    private MenuType menuType;
    @ManyToOne
    private MenuCategory menuCategory;
    @ManyToMany
    private List<Ingredient> ingredients;

    @Builder
    public Food(String name, int price, String description, String menuTypeValue) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.menuType = MenuType.from(menuTypeValue);
    }
}
