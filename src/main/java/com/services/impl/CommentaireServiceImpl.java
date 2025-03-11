package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.CommentaireDto;
import com.entities.Commentaire;
import com.mappers.CommentaireMapper;
import com.repositories.CommentaireRepository;
import com.repositories.PizzaRepository;
import com.services.CommentaireService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("commentaireService")
@Transactional
public class CommentaireServiceImpl implements CommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final PizzaRepository pizzaRepository;
    private final CommentaireMapper commentaireMapper;

    public CommentaireServiceImpl(CommentaireRepository commentaireRepository, PizzaRepository pizzaRepository, CommentaireMapper commentaireMapper) {
        this.commentaireRepository = commentaireRepository;
        this.pizzaRepository = pizzaRepository;
        this.commentaireMapper = commentaireMapper;
    }

    /**
     * Sauvegarde un commentaire en base de données.
     * @param commentaireDto Le commentaire à sauvegarder.
     * @return Une réponse contenant le commentaire sauvegardé ou une erreur.
     */
    @Override
    public ApiResponse<CommentaireDto> saveCommentaire(CommentaireDto commentaireDto) {
        if (commentaireDto.getIdPizza() == null) {
            return ApiResponse.error("L'ID de la pizza est obligatoire pour ajouter un commentaire.");
        }

        var pizza = pizzaRepository.findById(commentaireDto.getIdPizza()).orElse(null);

        if (pizza == null) {
            return ApiResponse.error("Impossible d'ajouter un commentaire : la pizza n'existe pas.");
        }

        Commentaire commentaire = commentaireMapper.toEntity(commentaireDto);
        commentaire.setPizza(pizza);

        commentaire = commentaireRepository.save(commentaire);

        return ApiResponse.success(commentaireMapper.toDto(commentaire), "Commentaire enregistré avec succès !");
    }


    /**
     * Récupère un commentaire par son ID.
     * @param id L'ID du commentaire.
     * @return Une réponse contenant le commentaire trouvé ou une erreur.
     */
    @Override
    public ApiResponse<CommentaireDto> getCommentaireById(Long id) {
        Optional<Commentaire> commentaire = commentaireRepository.findById(id);
        return commentaire.map(value -> ApiResponse.success(commentaireMapper.toDto(value), "Commentaire trouvé."))
                .orElseGet(() -> ApiResponse.error("Le commentaire avec l'ID " + id + " n'existe pas."));
    }

    /**
     * Récupère les commentaires d'une pizza via le repository Pizza.
     * @param idPizza L'ID de la pizza.
     * @return Liste des commentaires associés à la pizza.
     */
    @Override
    public ApiResponse<List<CommentaireDto>> getCommentaireByPizza(Long idPizza) {
        if (!pizzaRepository.existsById(idPizza)) {
            return ApiResponse.error("Impossible de récupérer les commentaires : la pizza n'existe pas.");
        }

        List<CommentaireDto> commentaires = pizzaRepository.findCommentairesByPizzaId(idPizza)
                .stream()
                .map(commentaireMapper::toDto)
                .toList();

        return ApiResponse.success(commentaires, "Commentaires récupérés avec succès.");
    }

    /**
     * Met à jour un commentaire existant.
     * @param id L'ID du commentaire.
     * @param commentaireDto Les nouvelles informations du commentaire.
     * @return Une réponse contenant le commentaire mis à jour ou une erreur.
     */
    @Override
    public ApiResponse<CommentaireDto> updateCommentaire(Long id, CommentaireDto commentaireDto) {
        if (!commentaireRepository.existsById(id)) {
            return ApiResponse.error("Le commentaire avec l'ID " + id + " n'existe pas.");
        }

        if (commentaireDto.getNote() > 5) {
            return ApiResponse.error("La note doit être inférieure ou égale à 5.");
        }

        commentaireDto.setId(id);
        Commentaire updatedCommentaire = commentaireRepository.save(commentaireMapper.toEntity(commentaireDto));
        return ApiResponse.success(commentaireMapper.toDto(updatedCommentaire), "Commentaire mis à jour.");
    }

    /**
     * Supprime un commentaire par son ID.
     * @param id L'ID du commentaire.
     * @return Une réponse confirmant la suppression ou une erreur.
     */
    @Override
    public ApiResponse<Void> deleteCommentaire(Long id) {
        if (!commentaireRepository.existsById(id)) {
            return ApiResponse.error("Le commentaire avec l'ID " + id + " n'existe pas.");
        }
        commentaireRepository.deleteById(id);
        return ApiResponse.success(null, "Commentaire supprimé avec succès.");
    }
}
