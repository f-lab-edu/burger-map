package burgermap.dto.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponseDto {
    private long reviewId;
    private String memberNickname;
    private long storeId;
    private String storeName;
    private long foodId;
    private String foodName;
    private String content;
}
