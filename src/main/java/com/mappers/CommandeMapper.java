package com.mappers;

import com.dtos.CommandeDto;
import com.entities.Commande;
import org.springframework.stereotype.Component;

@Component
public class CommandeMapper {

    public CommandeDto toDto(Commande commande) {
        if (commande == null) {
            return null;
        }
        return new CommandeDto(
                commande.getId(),
                commande.getNumeroCommande(),
                commande.getDate(),
                commande.getPizza() != null ? commande.getPizza().getId() : null
        );
    }

    public Commande toEntity(CommandeDto dto) {
        if (dto == null) {
            return null;
        }
        Commande commande = new Commande();
        commande.setId(dto.getId());
        commande.setNumeroCommande(dto.getNumeroCommande());
        commande.setDate(dto.getDate());
        return commande;
    }
}
