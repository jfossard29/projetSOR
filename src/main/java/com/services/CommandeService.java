package com.services;

import com.dtos.ApiResponse;
import com.dtos.CommandeDto;

import java.util.Collection;

/**
 * Interface définissant les opérations sur les commandes.
 */
public interface CommandeService {

    /**
     * Ajoute une nouvelle commande.
     * @param commandeDto Les informations de la commande à ajouter.
     * @return Une réponse contenant la commande ajoutée.
     */
    ApiResponse<CommandeDto> addCommande(CommandeDto commandeDto);

    /**
     * Récupère une commande par son identifiant.
     * @param id L'ID unique de la commande.
     * @return Une réponse contenant la commande trouvée ou une erreur si inexistante.
     */
    ApiResponse<CommandeDto> getCommandeById(Long id);

    /**
     * Récupère toutes les commandes d'un utilisateur.
     * @param idUser L'ID de l'utilisateur.
     * @return Une réponse contenant la collection de commandes associées à l'utilisateur.
     */
    ApiResponse<Collection<CommandeDto>> getCommandeByIdUser(Long idUser);

    /**
     * Récupère toutes les commandes d'un numero de commande.
     * @param numeroCommande Numero de commande.
     * @return Une réponse contenant la collection de commandes associées un numero commande.
     */
    ApiResponse<Collection<CommandeDto>> getCommandeByNumero(String numeroCommande);

}
