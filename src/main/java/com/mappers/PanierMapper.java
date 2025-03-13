package com.mappers;

import com.dtos.PanierDto;
import com.entities.Panier;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class PanierMapper {

    public PanierDto toDto(Panier panier) {
        if (panier == null) {
            return null;
        }

        PanierDto panierDto = new PanierDto();
        panierDto.setId(panier.getId());
        panierDto.setIdUser(panier.getUser().getId());
        panierDto.setPizzaCommandeIds(
                panier.getPizzaCommandes().stream()
                        .map(pizzaCommande -> pizzaCommande.getId())
                        .collect(Collectors.toList())
        );

        return panierDto;
    }

    public Panier toEntity(PanierDto panierDto) {
        if (panierDto == null) {
            return null;
        }

        Panier panier = new Panier();
        panier.setId(panierDto.getId());

        return panier;
    }
}
