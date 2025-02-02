package burgermap.service.image;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDImageNameGenerator extends ImageNameGenerator {
    @Override
    public String generateImageName(String fileName) {
        return UUID.randomUUID() + extractFileExt(fileName);
    }
}
