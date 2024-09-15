package burgermap.dto.food;

import burgermap.enums.BunType;
import burgermap.enums.PattyType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BurgerInfoRequestDto {
    private String name;
    private int price;
    private String description;
    private BunType bunType;
    private PattyType pattyType;
}
