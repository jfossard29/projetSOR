package com.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "pizza")
    private Collection<IngredientPrincipal> ingredientPrincipal;

    @OneToMany(mappedBy = "pizza")
    private Collection<Commentaire> commentaire;
}

