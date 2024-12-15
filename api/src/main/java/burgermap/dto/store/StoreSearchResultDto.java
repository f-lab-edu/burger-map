package burgermap.dto.store;

import burgermap.dto.food.FoodInfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StoreSearchResultDto {
    private List<StoreInfoDto> storeInfos;
    private List<FoodInfoDto> foodInfos;
}
