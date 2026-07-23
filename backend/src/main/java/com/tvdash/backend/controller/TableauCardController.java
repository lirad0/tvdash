package com.tvdash.backend.controller;

import com.tvdash.backend.exceptions.MSUnknownException;
import com.tvdash.backend.exceptions.MSUnsupportedMediaException;
import com.tvdash.backend.model.TableauCard;
import com.tvdash.backend.repository.TableauCardRepository;
import com.tvdash.backend.services.MinioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tableau/cards")
@CrossOrigin(origins = "*")
public class TableauCardController {
    private final TableauCardRepository repository;
    private final MinioService minioService;

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

        try (InputStream fs = file.getInputStream()) {
            
            try (String imageName = minioService.insertImage(fs, file.getSize()))  {
                card.setimageName(imageName);
            } catch (MSUnsupportedMediaException e) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Only PNG, JPG and SVG files are allowed.");
            } catch (MSUnknownException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
            }

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("The file has a problem.");
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
}
