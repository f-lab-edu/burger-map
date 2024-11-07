package burgermap.dto.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ImageUploadUrlDto {
    private final String imageUploadUrl;
    private final String imageName;
}
