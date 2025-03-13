package com.mappers;

import com.dtos.PizzaCommandeDto;
import com.entities.PizzaCommande;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class PizzaCommandeMapper {

    public PizzaCommandeDto toDto(PizzaCommande pizzaCommande) {
        if (pizzaCommande == null) {
            return null;
        }

        PizzaCommandeDto pizzaCommandeDto = new PizzaCommandeDto();
        pizzaCommandeDto.setId(pizzaCommande.getId());
        pizzaCommandeDto.setPizzaId(pizzaCommande.getPizza().getId());
        pizzaCommandeDto.setPanierId(pizzaCommande.getPanier().getId());
        pizzaCommandeDto.setQuantite(pizzaCommande.getQuantite());
        pizzaCommandeDto.setCommandeId(pizzaCommande.getCommande() != null ? pizzaCommande.getCommande().getId() : null);
        pizzaCommandeDto.setIngredientsOptionnelsIds(
                pizzaCommande.getIngredientsOptionnels().stream()
                        .map(ingredientOptionnel -> ingredientOptionnel.getIngredient().getId()) // Utilisation des IDs
                        .collect(Collectors.toList())
        );

        return pizzaCommandeDto;
    }

    public PizzaCommande toEntity(PizzaCommandeDto dto) {
        if (dto == null) {
            return null;
        }

        PizzaCommande pizzaCommande = new PizzaCommande();
        pizzaCommande.setId(dto.getId());
        pizzaCommande.setQuantite(dto.getQuantite());

        return pizzaCommande;
    }
}
