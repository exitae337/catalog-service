package ru.example.catalogservice.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.example.catalogservice.exception.NotFoundException;
import ru.example.catalogservice.exception.ServerErrorException;
import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.model.entity.ProductImage;
import ru.example.catalogservice.repository.ProductImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final MinioClient minioClient;

    @Value("${product.max-images}")
    private Integer maxImages;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${service.gateway.name}")
    private String gatewayServiceName;

    @Transactional
    public void attachImagesToProduct(Product product, List<String> imagesUrls) {
        List<ProductImage> productImages = imagesUrls.stream()
                .map(imageUrl -> ProductImage.builder()
                        .product(product)
                        .fileName(imageUrl)
                        .build())
                .toList();
        productImageRepository.saveAll(productImages);
    }

    public List<String> saveImagesInFileStorage(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        int size = Math.min(images.size(), maxImages);
        for (int i = 0; i < size; i++) {
            MultipartFile file = images.get(i);
            if (!file.isEmpty() && isImage(file)) {
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                String imageName = "%s.%s".formatted(UUID.randomUUID(), extension);
                try {
                    minioClient.putObject(PutObjectArgs.builder()
                            .object(imageName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());
                    imageUrls.add(imageName);
                } catch (Exception e) {
                    log.error("Error when saving an image to MinIO: {}", e.getMessage());
                }
            }
        }
        return imageUrls;
    }

    public byte[] getImageByFileName(String fileName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build())
                    .readAllBytes();
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                log.error("Изображение '{}' не найдено", fileName);
                throw new NotFoundException("Изображение '%s' не найдено".formatted(fileName));
            }
            log.error("Ошибка MinIO. Код ошибки: {}. Сообщение: {}", e.errorResponse().code(), e.getMessage());
            throw new ServerErrorException("Ошибка сервера");
        } catch (Exception e) {
            log.error("Ошибка MinIO. Сообщение: {}", e.getMessage());
            throw new ServerErrorException("Ошибка сервера");
        }
    }

    public List<String> getImagesUrls(List<ProductImage> productImages) {
        return productImages.stream()
                .map(productImage -> "%s/images/%s".formatted(gatewayServiceName, productImage.getFileName()))
                .toList();
    }

    private boolean isImage(MultipartFile file) {
        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            return List.of("png", "jpg", "jpeg").contains(extension);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
