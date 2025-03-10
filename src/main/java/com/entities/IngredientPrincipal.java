package com.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredient_principal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientPrincipal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_ingredient", nullable = false)
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "id_pizza", nullable = false)
    public Pizza pizza;
}

