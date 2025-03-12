package com.repositories;

import com.entities.PizzaCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PizzaCommandeRepository extends JpaRepository<PizzaCommande, Long> {
    List<PizzaCommande> findByPanierId(Long panierId);
}
