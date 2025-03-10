package com.services;

import com.dtos.PizzaDto;

import java.util.List;

/**
 * Interface définissant les opérations métier disponibles pour la gestion des pizzas.
 * Cette interface suit le principe d'Interface Segregation (SOLID).
 */
public interface PizzaService {
    /**
     * Sauvegarde une pizza dans le système
     * @param pizzaDto les données de la pizza à sauvegarder
     * @return la pizza sauvegardée avec son ID généré
     */
    PizzaDto savePizza(PizzaDto pizzaDto);

    /**
     * Récupère une pizza par son identifiant
     * @param pizzaId l'identifiant de la pizza recherchée
     * @return la pizza trouvée
     * @throws jakarta.persistence.EntityNotFoundException si la pizza n'existe pas
     */
    PizzaDto getPizzaById(Long pizzaId);

    /**
     * Supprime une pizza du système
     * @param pizzaId l'identifiant de la pizza à supprimer
     * @return true si la suppression a réussi
     */
    boolean deletePizza(Long pizzaId);

    /**
     * Récupère toutes les pizzas du système
     * @return la liste des pizzas
     */
    List<PizzaDto> getAllPizzas();

    PizzaDto updatePizza(Long pizzaId, PizzaDto pizzaDto);
}
