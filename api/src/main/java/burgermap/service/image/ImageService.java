package burgermap.service.image;

import burgermap.dto.image.ImageUploadUrlDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final ImageUploadURLPresigner presigner;
    private final ImageNameGenerator imageNameGenerator;

    /**
     * 이미지 업로드 URL 생성
     * 이미지를 업로드하지 않는 경우 Optional.empty 반환
     *
     * @param imageUploadDirectory 이미지 업로드 디렉토리
     * @param fileName      업로드할 이미지 파일명
     * @return 생성된 이미지 업로드 URL, 이미지 파일명
     */
    public Optional<ImageUploadUrlDto> createPresignedUploadUrl(String imageUploadDirectory, String fileName) {
        log.debug("presigned URL request: {} - {}", imageUploadDirectory, fileName);
        if (fileName == null) {
            return Optional.empty();
        }

        String imageUploadName = imageNameGenerator.generateImageName(fileName);
        String imageUploadPath = String.join("/", imageUploadDirectory, imageUploadName);

        String presignedUrl = presigner.getPresignedUrl(imageUploadPath);
        log.debug("presigned URL: {}", presignedUrl);

        return Optional.of(new ImageUploadUrlDto(presignedUrl, imageUploadName));
    }

    /**
     * 다수의 이미지 업로드 URL 생성
     * @param imageUploadDirectory 이미지 업로드 디렉토리
     * @param fileNames 업로드 이미지 파일명 리스트
     * @return 파일 원본 이름, (생성된 이미지 업로드 URL, 이미지 파일명) Map
     */
    public Map<String, ImageUploadUrlDto> createPresignedUploadUrls(String imageUploadDirectory, List<String> fileNames) {
        return fileNames.stream()
                .map(fileName -> Map.entry(fileName, createPresignedUploadUrl(imageUploadDirectory, fileName)))
                .filter(entry -> entry.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));
    }

    /**
     * 이미지 파일명과 카테고리로부터 이미지 URL 생성
     *
     * @param imageDirectory 이미지 업로드 디렉토리
     * @param imageName     이미지 파일명
     * @return 생성된 이미지 URL
     */
    public Optional<String> getImageUrl(String imageDirectory, String imageName) {
        if (imageName == null) {
            return Optional.empty();
        }
        return Optional.of(String.join(
                "/", presigner.getEndpoint(), presigner.getBucket(), imageDirectory, imageName));
    }
}
