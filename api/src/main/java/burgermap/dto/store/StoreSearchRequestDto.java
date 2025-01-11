package burgermap.dto.store;

import burgermap.dto.food.FoodFilter;
import burgermap.dto.geo.GeoLocationRange;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreSearchRequestDto {
    private GeoLocationRange geoLocationRange;
    private FoodFilter foodFilter;
}
