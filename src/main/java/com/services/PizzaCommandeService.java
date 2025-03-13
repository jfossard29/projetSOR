package com.services;

import com.dtos.ApiResponse;
import com.dtos.PizzaCommandeDto;

/**
 * Interface définissant les opérations sur les pizzas commandées.
 */
public interface PizzaCommandeService {

    /**
     * Récupère une pizza commandée par son identifiant.
     * @param id L'ID de la pizza commandée.
     * @return Une réponse contenant la pizza commandée ou une erreur si inexistante.
     */
    ApiResponse<PizzaCommandeDto> getPizzaCommandeById(Long id);

    /**
     * Ajoute une pizza à une commande.
     * @param pizzaCommande Les informations de la pizza à ajouter.
     * @return Une réponse contenant la pizza ajoutée ou une erreur si elle existe déjà dans le panier.
     */
    ApiResponse<PizzaCommandeDto> addPizzaCommande(PizzaCommandeDto pizzaCommande);

    /**
     * Met à jour les informations d'une pizza commandée.
     * @param pizzaCommande Les nouvelles informations de la pizza commandée.
     * @return Une réponse contenant la pizza mise à jour ou une erreur si inexistante.
     */
    ApiResponse<PizzaCommandeDto> updatePizzaCommande(Long pizzaCommandeId,PizzaCommandeDto pizzaCommande);

    /**
     * Supprime une pizza commandée de la base de données.
     * @param id L'ID de la pizza commandée à supprimer.
     * @return Une réponse confirmant la suppression ou une erreur si inexistante.
     */
    ApiResponse<Void> deletePizzaCommande(Long id);
}
