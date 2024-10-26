package burgermap.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageCategory {
    MEMBER_PROFILE_IMAGE("profile-images"),
    STORE_IMAGE("store-images"),
    FOOD_IMAGE("food-images"),
    REVIEW_IMAGE("review-images");

    private final String directory;
}
