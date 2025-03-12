package com.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "pizza_commande")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pizza", nullable = false)
    private Pizza pizza;

    @ManyToOne
    @JoinColumn(name = "id_panier", nullable = false)
    private Panier panier;

    private int quantite;

    @OneToOne
    @JoinColumn(name = "id_commande")
    private Commande commande;

    @OneToMany(mappedBy = "pizzaCommande", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference("pizza-optionnel")
    private Collection<IngredientOptionnel> ingredientsOptionnels = new ArrayList<>();
}
