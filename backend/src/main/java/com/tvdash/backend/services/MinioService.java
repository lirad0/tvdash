package com.tvdash.backend.services;

import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tvdash.backend.config.MinioProperties;
import com.tvdash.backend.exceptions.MSUnknownException;
import com.tvdash.backend.exceptions.MSUnsupportedMediaException;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MinioService {

    private static Set<String> ALLOWED_CONTENT_TYPES = Set.of(
        "image/png", "image/svg+xml", "image/jpeg"
    );

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final FileService fileService;

    @Value("${minio.bucket}")
    private String bucket;

    public String insertImage(InputStream image, long fileSize) {
        String contentType = fileService.getFileType(image);
        String imageName;

        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new MSUnsupportedMediaException();
        }

            String extension = resolveExtension(contentType);

            imageName = UUID.randomUUID() + extension;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(imageName)
                            .stream(image, fileSize, -1)
                            .contentType(contentType)
                            .build()
            );
                try (InputStream inputStream = file.getInputStream()) {


            
        } catch (Exception e) {
            throw new MSUnknownException();
        }

        return imageName;
    }

    // Streaming the image piece by piece
    public InputStream getImageStream(String imageName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(imageName)
                        .build());
    }

    public String getContentType(String objectName) throws Exception {
        StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build());

        return stat.contentType(); // e.g. "image/png"
    }

    private String resolveExtension(String contentType) {
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "image/svg+xml" -> ".svg";
            default -> "";
        };
    }
}