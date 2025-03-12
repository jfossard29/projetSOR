package com.repositories;

import com.entities.Statistique;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatistiqueRepository extends MongoRepository<Statistique, String> {
}
