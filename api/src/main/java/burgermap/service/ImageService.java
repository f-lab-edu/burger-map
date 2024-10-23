package burgermap.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {

    @Value("${cloud.ncp.object-storage.region}")
    private String region;
    @Value("${cloud.ncp.object-storage.endpoint}")
    private String endpoint;
    @Value("${cloud.ncp.object-storage.bucket}")
    private String bucket;
    @Value("${cloud.ncp.object-storage.access-key}")
    private String accessKey;
    @Value("${cloud.ncp.object-storage.secret-key}")
    private String secretKey;

    private S3Presigner s3Presigner;

    @PostConstruct
    public void init() {
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );

        s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public String createPresignedUploadUrl(ImageCategory imageCategory, String fileName) {
        log.debug("presigned URL request: {} - {}", imageCategory, fileName);
        String fileExt = fileName.substring(fileName.lastIndexOf("."));
        String directory = imageCategory.getDirectory();

        String uploadImagePath = "%s/%s%s".formatted(directory, UUID.randomUUID().toString(), fileExt);

        // ACL 설정 - 업로드한 이미지를 공개 설정
        // PutObjectRequest.builder().acl() 메서드는 presigned URL 생성 시 동작하지 않음.
        AwsRequestOverrideConfiguration overrideConfig = AwsRequestOverrideConfiguration.builder()
                .putRawQueryParameter("x-amz-acl", ObjectCannedACL.PUBLIC_READ.toString())
                .build();

        // 업로드할 이미지 정보를 담은 요청 객체 생성
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(uploadImagePath)
                .overrideConfiguration(overrideConfig)
                .build();
        // 생성할 URL의 특성 설정
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(30))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        log.debug("presigned URL: {}", presignedRequest.url().toString());

        return presignedRequest.url().toString();
    }

    @Getter
    @RequiredArgsConstructor
    public static enum ImageCategory {
        MEMBER_PROFILE_IMAGE("profile-images"),
        STORE_IMAGE("store-images"),
        FOOD_IMAGE("food-images"),
        REVIEW_IMAGE("review-images");

        private final String directory;
    }
}
