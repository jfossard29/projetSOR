package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.CommentaireDto;
import com.entities.Commentaire;
import com.entities.Pizza;
import com.entities.User;
import com.mappers.CommentaireMapper;
import com.repositories.CommentaireRepository;
import com.repositories.PizzaRepository;
import com.repositories.UserRepository;
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
    private final UserRepository userRepository;

    public CommentaireServiceImpl(CommentaireRepository commentaireRepository, PizzaRepository pizzaRepository, CommentaireMapper commentaireMapper, UserRepository userRepository) {
        this.commentaireRepository = commentaireRepository;
        this.pizzaRepository = pizzaRepository;
        this.commentaireMapper = commentaireMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse<CommentaireDto> saveCommentaire(CommentaireDto commentaireDto) {
        if (commentaireDto.getIdPizza() == null) {
            return ApiResponse.error("L'ID de la pizza est obligatoire pour ajouter un commentaire.");
        }

        Pizza pizza = pizzaRepository.findById(commentaireDto.getIdPizza()).orElse(null);
        if (pizza == null) {
            return ApiResponse.error("Impossible d'ajouter un commentaire : la pizza n'existe pas.");
        }

        User user = userRepository.findById(commentaireDto.getIdUser()).orElse(null);
        if (user == null) {
            return ApiResponse.error("Impossible d'ajouter un commentaire : l'utilisateur n'existe pas.");
        }

        Commentaire commentaire = commentaireMapper.toEntity(commentaireDto, pizza, user);
        commentaire = commentaireRepository.save(commentaire);

        if (commentaire != null) {
            return ApiResponse.success(commentaireMapper.toDto(commentaire), "Commentaire enregistré avec succès !");
        } else {
            return ApiResponse.error("Une erreur est survenue lors de l'enregistrement du commentaire.");
        }
    }

    @Override
    public ApiResponse<CommentaireDto> getCommentaireById(Long id) {
        Optional<Commentaire> commentaire = commentaireRepository.findById(id);
        return commentaire.map(value -> ApiResponse.success(commentaireMapper.toDto(value), "Commentaire trouvé."))
                .orElseGet(() -> ApiResponse.error("Le commentaire avec l'ID " + id + " n'existe pas."));
    }

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

    @Override
    public ApiResponse<CommentaireDto> updateCommentaire(Long id, CommentaireDto commentaireDto) {
        if (!commentaireRepository.existsById(id)) {
            return ApiResponse.error("Le commentaire avec l'ID " + id + " n'existe pas.");
        }

        if (commentaireDto.getNote() > 5) {
            return ApiResponse.error("La note doit être inférieure ou égale à 5.");
        }

        commentaireDto.setId(id);
        var pizza = pizzaRepository.findById(commentaireDto.getIdPizza()).orElse(null);
        if (pizza == null) {
            return ApiResponse.error("L'ID de la pizza n'existe pas.");
        }
        var user = userRepository.findById(commentaireDto.getIdUser()).orElse(null);
        if (user == null) {
            return ApiResponse.error("L'ID du Users n'existe pas.");
        }
        Commentaire updatedCommentaire = commentaireRepository.save(commentaireMapper.toEntity(commentaireDto,pizza,user));
        return ApiResponse.success(commentaireMapper.toDto(updatedCommentaire), "Commentaire mis à jour.");
    }

    @Override
    public ApiResponse<Void> deleteCommentaire(Long id) {
        if (!commentaireRepository.existsById(id)) {
            return ApiResponse.error("Le commentaire avec l'ID " + id + " n'existe pas.");
        }
        commentaireRepository.deleteById(id);
        return ApiResponse.success(null, "Commentaire supprimé avec succès.");
    }
}
