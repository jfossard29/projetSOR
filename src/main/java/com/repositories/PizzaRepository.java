package com.repositories;

import com.entities.Commentaire;
import com.entities.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'accès aux données des pizzas.
 */
@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    @Query("SELECT p.commentaire FROM Pizza p WHERE p.id = :idPizza")
    List<Commentaire> findCommentairesByPizzaId(@Param("idPizza") Long idPizza);
}
