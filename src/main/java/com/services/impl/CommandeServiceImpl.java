package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.CommandeDto;
import com.entities.Commande;
import com.entities.PizzaCommande;
import com.mappers.CommandeMapper;
import com.repositories.CommandeRepository;
import com.repositories.PizzaCommandeRepository;
import com.services.CommandeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service("CommandeService")
@Transactional
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final PizzaCommandeRepository pizzaCommandeRepository;
    private final CommandeMapper commandeMapper;

    public CommandeServiceImpl(CommandeRepository commandeRepository, PizzaCommandeRepository pizzaCommandeRepository,
                               CommandeMapper commandeMapper) {
        this.commandeRepository = commandeRepository;
        this.pizzaCommandeRepository = pizzaCommandeRepository;
        this.commandeMapper = commandeMapper;
    }

    @Override
    public ApiResponse<CommandeDto> createCommande(Long pizzaCommandeId) {
        PizzaCommande pizzaCommande = pizzaCommandeRepository.findById(pizzaCommandeId)
                .orElseThrow(() -> new EntityNotFoundException("Pizza commande introuvable"));

        Commande commande = new Commande();
        commande.setNumeroCommande("CMD-" + System.currentTimeMillis());
        commande.setDate(LocalDate.now());
        commande.setPizza(pizzaCommande);

        commande = commandeRepository.save(commande);
        return ApiResponse.success(commandeMapper.toDto(commande), "Commande créée avec succès !");
    }

    @Override
    public ApiResponse<CommandeDto> getCommandeById(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Commande introuvable"));
        return ApiResponse.success(commandeMapper.toDto(commande), "Commande trouvée !");
    }
}
