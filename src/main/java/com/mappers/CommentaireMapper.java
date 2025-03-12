package com.mappers;

import com.dtos.CommentaireDto;
import com.entities.Commentaire;
import com.entities.Pizza;
import com.entities.User;
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
        commentaireDto.setNote(commentaire.getNote());
        commentaireDto.setIdPizza(commentaire.getPizza() != null ? commentaire.getPizza().getId() : null);
        commentaireDto.setIdUser(commentaire.getUser() != null ? commentaire.getUser().getId() : null);

        return commentaireDto;
    }

    public Commentaire toEntity(CommentaireDto commentaireDto, Pizza pizza, User user) {
        if (commentaireDto == null) {
            return null;
        }

        Commentaire commentaire = new Commentaire();
        commentaire.setId(commentaireDto.getId());
        commentaire.setText(commentaireDto.getText());
        commentaire.setDate(commentaireDto.getDate());
        commentaire.setPhoto(commentaireDto.getPhoto());
        commentaire.setNote(commentaireDto.getNote());

        commentaire.setPizza(pizza);
        commentaire.setUser(user);

        return commentaire;
    }
}
