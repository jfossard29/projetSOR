package com.services;

import com.dtos.ApiResponse;
import com.dtos.CommentaireDto;

import java.util.List;

public interface CommentaireService {
    ApiResponse<CommentaireDto> saveCommentaire(CommentaireDto commentaireDto);
    ApiResponse<List<CommentaireDto>> getCommentaireByPizza(Long idPizza);
    ApiResponse<CommentaireDto> getCommentaireById(Long id);
    ApiResponse<CommentaireDto> updateCommentaire(Long id, CommentaireDto commentaireDto);
    ApiResponse<Void> deleteCommentaire(Long id);
}
