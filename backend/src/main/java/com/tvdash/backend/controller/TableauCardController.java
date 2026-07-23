package com.tvdash.backend.controller;

import com.tvdash.backend.config.MinioProperties;
import com.tvdash.backend.model.TableauCard;
import com.tvdash.backend.repository.TableauCardRepository;
import com.tvdash.backend.services.FileService;
import com.tvdash.backend.services.MinioService;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tableau/cards")
@CrossOrigin(origins = "*")
public class TableauCardController {
    private static Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/png", "image/svg+xml", "image/jpeg"
    );

    private final TableauCardRepository repository;
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final MinioService minioService;
    private final FileService fileService;

    @GetMapping
    public List<TableauCard> findAll() {
        return repository.findAll();
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> uploadCardImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam(value = "url", required = false) String url) {
                
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty.");
        }

        TableauCard card = new TableauCard();

        try (InputStream inputStream = file.getInputStream()) {
            String contentType = fileService.getFileType(inputStream);

            if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Only PNG, JPG and SVG files are allowed.");
            }

            String extension = resolveExtension(contentType);

            String imageName = UUID.randomUUID() + extension;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(imageName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );

            card.setimageName(imageName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }

        
        card.setName(name);
        card.setUrl(url);

        return ResponseEntity.ok(repository.save(card));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableauCard> findById(@PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableauCard> update(@PathVariable String id, @Valid @RequestBody TableauCard card) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(card.getName());
                    existing.setimageName(card.getimageName());
                    existing.setUrl(card.getUrl());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stream/{fileName}")
    public ResponseEntity<InputStreamResource> streamImage(@PathVariable String fileName) throws Exception {
        InputStream stream = minioService.getImageStream(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.)
                .body(new InputStreamResource(stream));
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
