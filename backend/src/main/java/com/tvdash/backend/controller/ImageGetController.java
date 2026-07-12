package com.tvdash.backend.controller;

import com.tvdash.backend.config.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/images")
public class ImageGetController {
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    @GetMapping(value = "/getList")
    public ResponseEntity<?> getImageList() {
        try {
            Iterator<Result<Item>> iteratorMinioResults =
                minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(
                        minioProperties.getBucket()
                    ).build()
                )
                .iterator();

            List<String> objectNames = StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(
                        iteratorMinioResults, Spliterator.ORDERED),
                        false
                    )
                    .map(result -> {
                        try {
                            return result.get().objectName();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(objectNames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve image list: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{objectName:.+}")
    public ResponseEntity<?> getImage(@PathVariable String objectName) {
        try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(objectName)
                        .build())) {
            MediaType contentType = resolveContentType(objectName);
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Image not found: " + e.getMessage());
        }
    }

    private MediaType resolveContentType(String objectName) {
        String lowerCaseName = objectName.toLowerCase();
        if (lowerCaseName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (lowerCaseName.endsWith(".svg")) {
            return MediaType.parseMediaType("image/svg+xml");
        }
        if (lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
