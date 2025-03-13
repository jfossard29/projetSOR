package com.mappers;

import com.dtos.CommandeDto;
import com.entities.Commande;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class CommandeMapper {

    public CommandeDto toDto(Commande commande) {
        if (commande == null) {
            return null;
        }

        Collection<Long> pizzaCommandeIds = new ArrayList<>();
        if (commande.getPizza() != null) {
            pizzaCommandeIds.add(commande.getPizza().getId());
        }

        CommandeDto commandeDto = new CommandeDto();
        commandeDto.setId(commande.getId());
        commandeDto.setNumeroCommande(commande.getNumeroCommande());
        commandeDto.setDate(commande.getDate());
        commandeDto.setPizzaCommandeIds(pizzaCommandeIds);
        commandeDto.setIdUser(commande.getUser().getId());
        return commandeDto;
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
