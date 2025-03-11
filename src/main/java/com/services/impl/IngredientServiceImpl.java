package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.IngredientDto;
import com.entities.Ingredient;
import com.mappers.IngredientMapper;
import com.repositories.IngredientRepository;
import com.services.IngredientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("IngredientService")
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    /**
     * Enregistre un ingrédient en base de données.
     * @param ingredientDto L'ingrédient à enregistrer.
     * @return Une réponse contenant l'ingrédient sauvegardé ou une erreur.
     */
    @Override
    public ApiResponse<IngredientDto> saveIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = ingredientMapper.toEntity(ingredientDto);
        ingredient = ingredientRepository.save(ingredient);
        return ApiResponse.success(ingredientMapper.toDto(ingredient), "Ingrédient enregistré avec succès !");
    }

    /**
     * Récupère un ingrédient par son ID.
     * @param ingredientId L'ID de l'ingrédient.
     * @return Une réponse contenant l'ingrédient ou une erreur s'il n'existe pas.
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<IngredientDto> getIngredientById(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("L'ingrédient avec l'ID %d n'existe pas", ingredientId)));

        return ApiResponse.success(ingredientMapper.toDto(ingredient), "Ingrédient trouvé.");
    }

    /**
     * Supprime un ingrédient par son ID.
     * @param ingredientId L'ID de l'ingrédient.
     * @return Une réponse confirmant la suppression ou une erreur.
     */
    @Override
    public ApiResponse<Void> deleteIngredient(Long ingredientId) {
        if (!ingredientRepository.existsById(ingredientId)) {
            return ApiResponse.error("L'ingrédient avec l'ID " + ingredientId + " n'existe pas.");
        }
        ingredientRepository.deleteById(ingredientId);
        return ApiResponse.success(null, "Ingrédient supprimé avec succès.");
    }

    /**
     * Récupère tous les ingrédients.
     * @return Une réponse contenant la liste des ingrédients existants.
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<IngredientDto>> getAllIngredients() {
        List<IngredientDto> ingredients = ingredientRepository.findAll().stream()
                .map(ingredientMapper::toDto)
                .toList();

        return ApiResponse.success(ingredients, "Liste des ingrédients récupérée avec succès.");
    }

    /**
     * Met à jour un ingrédient existant.
     * @param ingredientId L'ID de l'ingrédient.
     * @param ingredientDto Les nouvelles informations de l'ingrédient.
     * @return Une réponse contenant l'ingrédient mis à jour ou une erreur.
     */
    @Override
    public ApiResponse<IngredientDto> updateIngredient(Long ingredientId, IngredientDto ingredientDto) {
        Ingredient existingIngredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("L'ingrédient avec l'ID %d n'existe pas", ingredientId)));

        existingIngredient.setDescription(ingredientDto.getDescription());
        existingIngredient.setNom(ingredientDto.getNom());
        existingIngredient.setPhoto(ingredientDto.getPhoto());
        existingIngredient.setPrix(ingredientDto.getPrix());

        Ingredient updatedIngredient = ingredientRepository.save(existingIngredient);
        return ApiResponse.success(ingredientMapper.toDto(updatedIngredient), "Ingrédient mis à jour avec succès.");
    }
}
