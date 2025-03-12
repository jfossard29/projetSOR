package com.dtos;

import com.entities.Commentaire;
import com.entities.Ingredient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PizzaDto {

    @NotNull
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    private String photo;

    @NotBlank(message = "Le prix est obligatoire")
    private Double prix;

    @NotNull
    private Collection<IngredientDto> ingredients_principaux;

    private Collection<Commentaire> commentaire = new ArrayList<>();


}
