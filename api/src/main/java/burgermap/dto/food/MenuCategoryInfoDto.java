package burgermap.dto.food;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MenuCategoryInfoDto {
    private Long menuCategoryId;
    private String name;
}
