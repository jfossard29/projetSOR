package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.CommandeDto;
import com.entities.Commande;
import com.mappers.CommandeMapper;
import com.repositories.CommandeRepository;
import com.services.CommandeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("CommandeService")
@Transactional
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final CommandeMapper commandeMapper;

    public CommandeServiceImpl(CommandeRepository commandeRepository, CommandeMapper commandeMapper) {
        this.commandeRepository = commandeRepository;
        this.commandeMapper = commandeMapper;
    }

    @Override
    public ApiResponse<CommandeDto> addCommande(CommandeDto commandeDto) {
        Commande commande = commandeMapper.toEntity(commandeDto);
        commandeRepository.save(commande);

        return ApiResponse.success(commandeMapper.toDto(commande), "Commande ajoutée avec succès !");
    }

    @Override
    public ApiResponse<CommandeDto> getCommandeById(Long id) {
        Optional<Commande> commandeOpt = commandeRepository.findById(id);
        if (commandeOpt.isEmpty()) {
            return ApiResponse.error("Commande introuvable");
        }

        return ApiResponse.success(commandeMapper.toDto(commandeOpt.get()), "Commande trouvée !");
    }

    @Override
    public ApiResponse<Collection<CommandeDto>> getCommandeByIdUser(Long idUser) {
        Collection<Commande> commandes = commandeRepository.findByUserId(idUser);
        Collection<CommandeDto> commandeDtos = commandes.stream()
                .map(commande -> commandeMapper.toDto(commande))
                .collect(Collectors.toList());

        return ApiResponse.success(commandeDtos, "Commandes récupérées avec succès !");
    }

    @Override
    public ApiResponse<Collection<CommandeDto>> getCommandeByNumero(String numeroCommande) {
        Collection<Commande> commandes = commandeRepository.findByNumeroCommande(numeroCommande);
        Collection<CommandeDto> commandeDtos = commandes.stream()
                .map(commande -> commandeMapper.toDto(commande))
                .collect(Collectors.toList());

        return ApiResponse.success(commandeDtos, "Commandes récupérées avec succès !");
    }
}
