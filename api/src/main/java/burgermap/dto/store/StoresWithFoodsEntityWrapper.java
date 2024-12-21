package burgermap.dto.store;

import burgermap.entity.Food;
import burgermap.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoresWithFoodsEntityWrapper {
    private List<Store> stores;
    private List<Food> foods;
}
