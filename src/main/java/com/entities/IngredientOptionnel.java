package com.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ingredient_optionnel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientOptionnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pizza_commande", nullable = false)
    @JsonBackReference("pizza-optionnel")
    private PizzaCommande pizzaCommande;

    @ManyToOne
    @JoinColumn(name = "id_ingredient", nullable = false)
    private Ingredient ingredient;
}
