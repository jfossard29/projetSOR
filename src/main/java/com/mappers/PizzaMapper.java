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

        pizzaDto.setIngredients_principaux(
                pizza.getIngredients_principaux()
                        .stream()
                        .map(IngredientPrincipal::getIngredient)
                        .map(ingredientMapper::toDto)
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
        return pizza;
    }
}
