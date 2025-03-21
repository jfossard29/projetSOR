package com.repositories;

import com.entities.Panier;
import com.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PanierRepository extends JpaRepository<Panier, Long> {
    Optional<Panier> findByUserId(Long userId);
    boolean existsByUserId(Long userId);

    Panier findByUser(User user);
}
