package com.entities;

import com.dtos.IngredientDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "pizza")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private String photo;
    private Double prix;

    @OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference("pizza-ingredients")
    private Collection<IngredientPrincipal> ingredients_principaux = new ArrayList<>();

    @OneToMany(mappedBy = "pizza")
    @JsonBackReference("pizza-comments")
    private Collection<Commentaire> commentaire = new ArrayList<>();
}


