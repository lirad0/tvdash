package com.tvdash.backend.controller;

import com.tvdash.backend.model.TableauCard;
import com.tvdash.backend.repository.TableauCardRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tableau/cards")
@CrossOrigin(origins = "*")
public class TableauCardController {
    private final TableauCardRepository repository;

    public TableauCardController(TableauCardRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TableauCard> findAll() {
        return repository.findAll();
    }

    @PostMapping
    public TableauCard create(@Valid @RequestBody TableauCard card) {
        return repository.save(card);
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
                    existing.setImageUrl(card.getImageUrl());
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
