package ru.example.catalogservice.util;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Component
public class ContentTypeResolver {

    public MediaType resolveContentType(String fileName) {
        return switch (Objects.requireNonNull(StringUtils.getFilenameExtension(fileName))) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            default -> throw new IllegalArgumentException("Неверное расширение файла");
        };
    }
}
