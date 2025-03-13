package com.repositories;

import com.entities.PizzaCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PizzaCommandeRepository extends JpaRepository<PizzaCommande, Long> {
    List<PizzaCommande> findByPanierId(Long panierId);

    void deleteByPanierId(Long panierId);

    void deleteAllByPanierId(Long idPanier);

    Optional<PizzaCommande> findByPizzaIdAndPanierId(Long pizzaId, Long panierId);
}
