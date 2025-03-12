package com.mappers;

import com.dtos.PanierDto;
import com.entities.Panier;
import org.springframework.stereotype.Component;

@Component
public class PanierMapper {

    /**
     * Convertit une entité Panier en DTO.
     * @param panier L'entité Panier.
     * @return Le DTO PanierDto.
     */
    public PanierDto toDto(Panier panier) {
        if (panier == null) {
            return null;
        }
        return new PanierDto(panier.getId(), panier.getUser().getId());
    }

    /**
     * Convertit un DTO en entité Panier.
     * @param panierDto Le DTO PanierDto.
     * @return L'entité Panier.
     */
    public Panier toEntity(PanierDto panierDto) {
        if (panierDto == null) {
            return null;
        }
        Panier panier = new Panier();
        panier.setId(panierDto.getId());
        return panier;
    }
}
