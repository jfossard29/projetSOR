package com.mappers;

import com.dtos.PizzaCommandeDto;
import com.entities.PizzaCommande;
import org.springframework.stereotype.Component;

@Component
public class PizzaCommandeMapper {

    public PizzaCommandeDto toDto(PizzaCommande pizzaCommande) {
        if (pizzaCommande == null) {
            return null;
        }
        return new PizzaCommandeDto(
                pizzaCommande.getId(),
                pizzaCommande.getPizza().getId(),
                pizzaCommande.getPanier().getId(),
                pizzaCommande.getQuantite(),
                pizzaCommande.getCommande() != null ? pizzaCommande.getCommande().getId() : null
        );
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
