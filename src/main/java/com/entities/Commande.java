package com.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "commande")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroCommande;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "pizza_id", nullable = false)
    private Pizza pizza;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}

