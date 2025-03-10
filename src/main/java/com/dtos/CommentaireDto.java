package com.dtos;

import com.entities.Pizza;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentaireDto {
    @NotNull
    private Long id;

    @NotBlank(message = "Le texte ne peut pas être vide")
    private String text;
    private String photo;
    private LocalDate date;
    private int note;
    @NotNull
    private Pizza pizza;
}
