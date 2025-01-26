package burgermap.service.image;

import burgermap.dto.image.ImageUploadUrlDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;
    @Mock
    private ImageUploadURLPresigner presigner;
    @Mock
    private ImageNameGenerator imageNameGenerator;

    @Test
    @DisplayName("다수의 이미지 파일명에 대한 이미지 업로드 URL 생성")
    void createPresignedUploadUrls() {
        // given
        String imageUploadDirectory = "images";
        List<String> fileNames = List.of("image1.jpg", "image2.png", "image3.gif");
        List<String> newFileNames = List.of(
                "generated-image1.jpg", "generated-image2.png", "generated-image3.gif");
        List<String> urls = List.of(
                "https://presigned-url1.com", "https://presigned-url2.com", "https://presigned-url3.com");

        for (int imgIdx = 0; imgIdx < fileNames.size(); imgIdx++) {
            Mockito.when(imageNameGenerator.generateImageName(fileNames.get(imgIdx)))
                    .thenReturn(newFileNames.get(imgIdx));
            Mockito.when(presigner.getPresignedUrl(imageUploadDirectory + "/" + newFileNames.get(imgIdx)))
                    .thenReturn(urls.get(imgIdx));
        }

        // when
        Map<String, ImageUploadUrlDto> presignedUploadUrls
                = imageService.createPresignedUploadUrls(imageUploadDirectory, fileNames);

        // then
        // 원본 이미지 파일명과 생성된 파일명 & 이미지 업로드 URL를 담은 DTO가 매칭되어야 함
        ImageUploadUrlDto imageUploadUrlDto0 = presignedUploadUrls.get(fileNames.get(0));
        assertThat(imageUploadUrlDto0.getImageName()).isEqualTo(newFileNames.get(0));
        assertThat(imageUploadUrlDto0.getImageUploadUrl()).isEqualTo(urls.get(0));

        ImageUploadUrlDto imageUploadUrlDto1 = presignedUploadUrls.get(fileNames.get(1));
        assertThat(imageUploadUrlDto1.getImageName()).isEqualTo(newFileNames.get(1));
        assertThat(imageUploadUrlDto1.getImageUploadUrl()).isEqualTo(urls.get(1));

        ImageUploadUrlDto imageUploadUrlDto2 = presignedUploadUrls.get(fileNames.get(2));
        assertThat(imageUploadUrlDto2.getImageName()).isEqualTo(newFileNames.get(2));
        assertThat(imageUploadUrlDto2.getImageUploadUrl()).isEqualTo(urls.get(2));
    }

    @Test
    @DisplayName("업로드된 이미지 파일명이 주어지면 이미지 URL 생성")
    void getImageUrl() {
        // given
        String imageDirectory = "images";
        String imageName = "generated-image.jpg";

        String bucket = "burgermap";
        String endpoint = "https://burgermap.endpoint.com";
        String correctImageUrl = String.join("/", endpoint, bucket, imageDirectory, imageName);

        Mockito.when(presigner.getBucket()).thenReturn(bucket);
        Mockito.when(presigner.getEndpoint()).thenReturn(endpoint);

        // when
        Optional<String> imageUrl = imageService.getImageUrl(imageDirectory, imageName);

        // then
        assertThat(imageUrl).isPresent();
        assertThat(imageUrl.get()).isEqualTo(correctImageUrl);
    }
}
