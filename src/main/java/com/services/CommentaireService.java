package com.services;

import com.dtos.ApiResponse;
import com.dtos.CommentaireDto;
import java.util.List;

/**
 * L'interface CommentaireService fournit des méthodes pour gérer les commentaires
 * liés aux pizzas. Elle inclut des opérations pour sauvegarder, récupérer, mettre à jour
 * et supprimer des commentaires.
 */
public interface CommentaireService {

    /**
     * Sauvegarde un nouveau commentaire.
     *
     * @param commentaireDto L'objet CommentaireDto contenant les détails du commentaire à sauvegarder.
     * @return Une ApiResponse contenant l'objet CommentaireDto sauvegardé.
     */
    ApiResponse<CommentaireDto> saveCommentaire(CommentaireDto commentaireDto);

    /**
     * Récupère une liste de commentaires associés à une pizza spécifique.
     *
     * @param idPizza L'ID de la pizza pour laquelle récupérer les commentaires.
     * @return Une ApiResponse contenant une liste d'objets CommentaireDto associés à la pizza.
     */
    ApiResponse<List<CommentaireDto>> getCommentaireByPizza(Long idPizza);

    /**
     * Récupère un commentaire par son ID.
     *
     * @param id L'ID du commentaire à récupérer.
     * @return Une ApiResponse contenant l'objet CommentaireDto correspondant à l'ID donné.
     */
    ApiResponse<CommentaireDto> getCommentaireById(Long id);

    /**
     * Met à jour un commentaire existant.
     *
     * @param id L'ID du commentaire à mettre à jour.
     * @param commentaireDto L'objet CommentaireDto contenant les détails mis à jour du commentaire.
     * @return Une ApiResponse contenant l'objet CommentaireDto mis à jour.
     */
    ApiResponse<CommentaireDto> updateCommentaire(Long id, CommentaireDto commentaireDto);

    /**
     * Supprime un commentaire par son ID.
     *
     * @param id L'ID du commentaire à supprimer.
     * @return Une ApiResponse indiquant le résultat de l'opération de suppression.
     */
    ApiResponse<Void> deleteCommentaire(Long id);
}
