package burgermap.service.image;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cloud.ncp.object-storage")
@Setter
@Getter
@RequiredArgsConstructor
class ImageUploadUrlPresignerProperties {
    private final String region;
    private final String endpoint;
    private final String bucket;
    private final String accessKey;
    private final String secretKey;
}
