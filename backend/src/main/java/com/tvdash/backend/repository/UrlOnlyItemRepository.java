package com.tvdash.backend.repository;

import com.tvdash.backend.model.UrlOnlyItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlOnlyItemRepository extends MongoRepository<UrlOnlyItem, String> {
}
