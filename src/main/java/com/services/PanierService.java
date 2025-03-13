package com.services;

import com.dtos.ApiResponse;
import com.dtos.CommandeDto;
import com.dtos.PanierDto;

/**
 * Interface définissant les opérations sur les paniers.
 */
public interface PanierService {

    /**
     * Crée un nouveau panier pour un utilisateur.
     * @param panierDto Les informations du panier à créer.
     * @return Une réponse contenant le panier créé.
     */
    ApiResponse<PanierDto> creerPanier(PanierDto panierDto);

    /**
     * Vide un panier en supprimant toutes ses pizzas commandées.
     * @param idPanier L'ID du panier à vider.
     * @return Une réponse confirmant l'opération.
     */
    ApiResponse<PanierDto> viderPanier(Long idPanier);

    /**
     * Récupère le panier actif d'un utilisateur.
     * @param idUser L'ID de l'utilisateur.
     * @return Une réponse contenant le panier trouvé ou une erreur si l'utilisateur n'a pas de panier actif.
     */
    ApiResponse<PanierDto> getPanierByIdUser(Long idUser);

    /**
     * Fusionne un panier temporaire avec le panier existant d'un utilisateur.
     * @param userId L'ID de l'utilisateur.
     * @param panierDto Le panier temporaire à fusionner.
     * @return Une réponse contenant le panier mis à jour.
     */
    ApiResponse<PanierDto> fusionPanier(Long userId, PanierDto panierDto);

    /**
     * Valide un panier et le transforme en commande.
     * - Pour chaque pizza du panier, une ligne est ajoutée dans `Commande`.
     * - Toutes les pizzas du même panier obtiennent le même `numeroCommande`.
     * - Le panier est vidé mais non supprimé.
     * - Mise à jour des statistiques (nombre de commandes par pizza, ingrédient et utilisateur).
     *
     * @param idPanier L'ID du panier à valider.
     * @return Une réponse contenant la commande créée.
     */
    ApiResponse<CommandeDto> validerPanier(Long idPanier);
}
