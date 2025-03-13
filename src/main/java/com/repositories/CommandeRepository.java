package com.repositories;

import com.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    Collection<Commande> findByNumeroCommande(String numeroCommande);

    Collection<Commande> findByUserId(Long id);
}
