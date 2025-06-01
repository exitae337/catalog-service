package ru.example.catalogservice.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.username}")
    private String username;

    @Value("${minio.password}")
    private String password;

    @Value("${minio.bucket}")
    private String bucket;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(username, password)
                .build();
        createBucket(minioClient);
        return minioClient;
    }

    private void createBucket(MinioClient minioClient) {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucket)
                        .build());
            }
        } catch (Exception e) {
            log.error("Ошибка MinIO: {}", e.getMessage());
        }
    }
}
