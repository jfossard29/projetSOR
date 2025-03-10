package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.CommentaireDto;
import com.entities.Commentaire;
import com.mappers.CommentaireMapper;
import com.repositories.CommentaireRepository;
import com.services.CommentaireService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("commentaireService")
@Transactional
public class CommentaireServiceImpl implements CommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final CommentaireMapper commentaireMapper;

    public CommentaireServiceImpl(CommentaireRepository commentaireRepository, CommentaireMapper commentaireMapper) {
        this.commentaireRepository = commentaireRepository;
        this.commentaireMapper = commentaireMapper;
    }

    /**
     * Sauvegarde un commentaire en base de données.
     * @param commentaireDto Le commentaire à sauvegarder.
     * @return Une réponse contenant le commentaire sauvegardé ou une erreur.
     */
    @Override
    public ApiResponse<CommentaireDto> saveCommentaire(CommentaireDto commentaireDto) {
        //TODO Vérifier possible mettre un commentaire avec article
        //TODO Verifier que note inferieur a 5
        Commentaire commentaire = commentaireMapper.toEntity(commentaireDto);
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

    @Override
    public ApiResponse<List<CommentaireDto>> getCommentaireByPizza(Long idPizza) {
        //TODO faire avec repository Pizza pour recupere tous les commentaire d'un pizza
        return null;
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
        //Todo verifier que la note inferieur a 5
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
