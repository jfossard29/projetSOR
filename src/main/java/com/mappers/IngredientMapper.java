package com.mappers;

import com.dtos.IngredientDto;
import com.entities.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {

    public IngredientDto toDto(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(ingredient.getId());
        ingredientDto.setNom(ingredient.getNom());
        ingredientDto.setDescription(ingredient.getDescription());
        ingredientDto.setPhoto(ingredient.getPhoto());
        ingredientDto.setPrix(ingredient.getPrix());
        return ingredientDto;
    }

    public Ingredient toEntity(IngredientDto ingredientDto) {
        if (ingredientDto == null) {
            return null;
        }

        Ingredient ingredient = new Ingredient();
        // On ne set l'ID que s'il existe (cas d'une mise à jour)
        if (ingredientDto.getId() != null) {
            ingredient.setId(ingredientDto.getId());
        }
        ingredient.setNom(ingredientDto.getNom());
        ingredient.setDescription(ingredientDto.getDescription());
        ingredient.setPhoto(ingredientDto.getPhoto());
        ingredient.setPrix(ingredientDto.getPrix());
        return ingredient;
    }
}
