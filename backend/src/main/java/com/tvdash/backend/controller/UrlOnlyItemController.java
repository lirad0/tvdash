package com.tvdash.backend.controller;

import com.tvdash.backend.model.UrlOnlyItem;
import com.tvdash.backend.repository.UrlOnlyItemRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tableau/url-only-items")
@CrossOrigin(origins = "*")
public class UrlOnlyItemController {
    private final UrlOnlyItemRepository repository;

    public UrlOnlyItemController(UrlOnlyItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<UrlOnlyItem> findAll() {
        return repository.findAll();
    }

    @PostMapping
    public UrlOnlyItem create(@Valid @RequestBody UrlOnlyItem item) {
        return repository.save(item);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlOnlyItem> findById(@PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UrlOnlyItem> update(@PathVariable String id, @Valid @RequestBody UrlOnlyItem item) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setUrl(item.getUrl());
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
