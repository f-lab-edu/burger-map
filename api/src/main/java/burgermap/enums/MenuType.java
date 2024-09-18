package burgermap.enums;

import burgermap.exception.food.FoodAttributeNotExistException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuType {
    MAIN("메인 메뉴"),
    SIDE("사이드 메뉴"),
    DRINK("음료");

    private final String value;

    public static MenuType from(String value) {
        for (MenuType menuType : MenuType.values()) {
            if (menuType.getValue().equals(value)) {
                return menuType;
            }
        }
        throw new FoodAttributeNotExistException("Menu Type %s does not exist".formatted(value));
    }
}
