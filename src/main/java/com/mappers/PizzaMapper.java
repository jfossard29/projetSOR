package com.mappers;

import com.dtos.PizzaDto;
import com.entities.IngredientPrincipal;
import com.entities.Pizza;
import org.springframework.stereotype.Component;

@Component
public class PizzaMapper {

    private final IngredientMapper ingredientMapper;

    public PizzaMapper(IngredientMapper ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
    }

    public PizzaDto toDto(Pizza pizza) {
        if (pizza == null) {
            return null;
        }

        PizzaDto pizzaDto = new PizzaDto();
        pizzaDto.setId(pizza.getId());
        pizzaDto.setNom(pizza.getNom());
        pizzaDto.setDescription(pizza.getDescription());
        pizzaDto.setPhoto(pizza.getPhoto());
        pizzaDto.setPrix(pizza.getPrix());

        // Charger directement les "Ingrédients" depuis IngredientPrincipal
        pizzaDto.setIngredients_principaux(
                pizza.getIngredients_principaux()
                        .stream()
                        .map(IngredientPrincipal::getIngredient) // Obtenir l'ingrédient depuis IngredientPrincipal
                        .map(ingredientMapper::toDto) // Mapper l'Ingrédient en DTO
                        .toList()
        );

        pizzaDto.setCommentaire(pizza.getCommentaire());
        return pizzaDto;
    }

    public Pizza toEntity(PizzaDto pizzaDto) {
        if (pizzaDto == null) {
            return null;
        }

        Pizza pizza = new Pizza();
        if (pizzaDto.getId() != null) {
            pizza.setId(pizzaDto.getId());
        }
        pizza.setNom(pizzaDto.getNom());
        pizza.setDescription(pizzaDto.getDescription());
        pizza.setPhoto(pizzaDto.getPhoto());
        pizza.setPrix(pizzaDto.getPrix());
        pizza.setCommentaire(pizzaDto.getCommentaire());

        // Liaison avec les Ingredients_principaux via la table IngredientPrincipal
        if (pizzaDto.getIngredients_principaux() != null) {
            pizza.setIngredients_principaux(
                    pizzaDto.getIngredients_principaux().stream().map(ingredientDto -> {
                        IngredientPrincipal ingredientPrincipal = new IngredientPrincipal();
                        ingredientPrincipal.setPizza(pizza); // Relier la pizza
                        ingredientPrincipal.setIngredient(ingredientMapper.toEntity(ingredientDto)); // Associer un ingrédient
                        return ingredientPrincipal;
                    }).toList()
            );
        }
        // Liaison avec les Ingredients_principaux via la table IngredientPrincipal
        //TODO : La liaison

        return pizza;
    }
}
