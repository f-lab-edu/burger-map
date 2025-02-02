package burgermap.service.image;

abstract class ImageNameGenerator {
    String extractFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    };

    /**
     * 이미지 파일명 생성 - 익명화 처리 위해 새로운 파일명 생성
     *
     * @param fileName 업로드 이미지 파일명
     * @return 생성된 이미지 파일명
     */
    abstract String generateImageName(String fileName);
}
