package com.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant un utilisateur dans le système.
 *
 * Cette entité est utilisée pour stocker les informations des utilisateurs, y compris
 * leur nom d'utilisateur et leur mot de passe (stocké sous forme de hachage).
 */
@Entity
@Table(name = "compte")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Identifiant unique de l'utilisateur.
     *
     * Ce champ est la clé primaire de l'entité utilisateur.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom d'utilisateur de l'utilisateur.
     *
     * Ce champ doit être non nul, et sert à identifier un utilisateur de manière unique dans le système.
     */
    @Column(nullable = false)
    private String nom;

    /**
     * Mot de passe haché de l'utilisateur.
     *
     * Ce champ contient le mot de passe de l'utilisateur après avoir été haché pour des raisons de sécurité.
     */
    @Column(nullable = false)
    private String mdp;

    /**
     * Idendité de l'utilisateur.
     *
     * Booleen servant à déterminer les droits du compte.
     */
    @Column(nullable = false)
    private Boolean estClient;

    private String adressePostale;

    @Column(nullable = false)
    private String adresseEmail;

}
