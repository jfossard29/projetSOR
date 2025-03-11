package com.repositories;

import com.entities.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repository pour l'accès aux données des commentaires.
 */
@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
}
