package com.services;

import com.dtos.ApiResponse;
import com.dtos.IngredientDto;

import java.util.List;

/**
 * Interface définissant les opérations métier disponibles pour la gestion des ingrédients.
 * Cette interface suit le principe d'Interface Segregation (SOLID).
 */
public interface IngredientService {
    /**
     * Sauvegarde un ingrédient dans le système
     * @param ingredientDto les données de l'ingrédient à sauvegarder
     * @return l'ingrédient sauvegardé avec son ID généré
     */
    ApiResponse<IngredientDto> saveIngredient(IngredientDto ingredientDto);

    /**
     * Récupère un ingrédient par son identifiant
     * @param ingredientId l'identifiant de l'ingrédient recherché
     * @return l'ingrédient trouvé
     * @throws jakarta.persistence.EntityNotFoundException si l'ingrédient n'existe pas
     */
    ApiResponse<IngredientDto> getIngredientById(Long ingredientId);

    /**
     * Supprime un ingrédient du système
     * @param ingredientId l'identifiant de l'ingrédient à supprimer
     * @return true si la suppression a réussi
     */
    ApiResponse<Void> deleteIngredient(Long ingredientId);

    /**
     * Récupère tous les ingrédients du système
     * @return la liste des ingrédients
     */
    ApiResponse<List<IngredientDto>> getAllIngredients();

    ApiResponse<IngredientDto> updateIngredient(Long ingredientId, IngredientDto ingredientDto);
}
