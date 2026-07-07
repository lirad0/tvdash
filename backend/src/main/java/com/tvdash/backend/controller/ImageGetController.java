package com.tvdash.backend.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tvdash.backend.config.MinioProperties;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;

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
}
