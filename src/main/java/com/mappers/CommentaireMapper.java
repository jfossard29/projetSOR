package com.mappers;

import com.dtos.CommentaireDto;
import com.dtos.DogDto;
import com.entities.Commentaire;
import com.entities.Dog;
import org.springframework.stereotype.Component;

@Component
public class CommentaireMapper {
    public CommentaireDto toDto(Commentaire commentaire) {
        if (commentaire == null) {
            return null;
        }
        CommentaireDto commentaireDto = new CommentaireDto();
        commentaireDto.setId(commentaire.getId());
        commentaireDto.setText(commentaire.getText());
        commentaireDto.setDate(commentaire.getDate());
        commentaireDto.setPhoto(commentaire.getPhoto());
        commentaireDto.setPizza(commentaire.getPizza());
        commentaireDto.setNote(commentaire.getNote());
        return commentaireDto;
    }

    public Commentaire toEntity(CommentaireDto commentaireDto) {
        if (commentaireDto == null) {
            return null;
        }
        Commentaire commentaire = new Commentaire();
        commentaire.setId(commentaireDto.getId());
        commentaire.setText(commentaireDto.getText());
        commentaire.setDate(commentaireDto.getDate());
        commentaire.setPhoto(commentaireDto.getPhoto());
        commentaire.setPizza(commentaireDto.getPizza());
        commentaire.setNote(commentaireDto.getNote());
        return commentaire;
    }
}
