package com.repositories;

import com.entities.Ingredient;
import com.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'accès aux données des utilisateurs.
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
