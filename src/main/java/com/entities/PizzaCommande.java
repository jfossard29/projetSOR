package com.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public Pizza pizza;

    @ManyToOne
    @JoinColumn(name = "id_panier", nullable = false)
    private Panier panier;

    private int quantite;

    @ManyToOne
    @JoinColumn(name = "id_commande")
    private Commande commande;

}
