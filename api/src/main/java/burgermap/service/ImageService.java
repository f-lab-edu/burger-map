package burgermap.service;

import burgermap.enums.ImageCategory;
import jakarta.annotation.PostConstruct;
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

    /**
     * 초기화 메서드 - AWS S3 Presigner 객체 생성
     */
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

    /**
     * 이미지 업로드를 위한 presigned URL 생성
     *
     * @param imageCategory 이미지 카테고리 (회원 프로필, 가게 소개 이미지, 음식 이미지, 리뷰 첨부 이미지)
     * @param fileName      업로드 이미지 파일명
     * @return presigned URL
     */
    public String createPresignedUploadUrl(ImageCategory imageCategory, String fileName) {
        log.debug("presigned URL request: {} - {}", imageCategory, fileName);

        String imageUploadPath = generateImageName(imageCategory, fileName);

        // ACL 설정 - 업로드한 이미지를 공개 설정
        // PutObjectRequest.builder().acl() 메서드는 presigned URL 생성 시 동작하지 않음.
        AwsRequestOverrideConfiguration overrideConfig = AwsRequestOverrideConfiguration.builder()
                .putRawQueryParameter("x-amz-acl", ObjectCannedACL.PUBLIC_READ.toString())
                .build();

        // 업로드할 이미지 정보를 담은 요청 객체 생성
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(imageUploadPath)
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

    /**
     * 이미지 파일명 생성
     *
     * @param imageCategory 이미지 카테고리
     * @param fileName      업로드 이미지 파일명
     * @return 생성된 이미지 파일명
     */
    private String generateImageName(ImageCategory imageCategory, String fileName) {
        String fileExt = fileName.substring(fileName.lastIndexOf("."));
        String directory = imageCategory.getDirectory();

        return "%s/%s%s".formatted(directory, UUID.randomUUID().toString(), fileExt);
    }
}
