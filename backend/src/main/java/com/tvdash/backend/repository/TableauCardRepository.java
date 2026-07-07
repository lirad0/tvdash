package com.tvdash.backend.repository;

import com.tvdash.backend.model.TableauCard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TableauCardRepository extends MongoRepository<TableauCard, String> {
}
