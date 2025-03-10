package com.dtos;

import lombok.*;

/**
 * DTO utilisé pour transférer les données d'un utilisateur.
 *
 * Ce DTO contient les informations nécessaires pour gérer un utilisateur, comme son nom d'utilisateur et son mot de passe haché.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /**
     * Le nom d'utilisateur de l'utilisateur.
     *
     * Ce champ est utilisé pour identifier de manière unique l'utilisateur.
     */
    private String nom;

    /**
     * Le mot de passe haché de l'utilisateur.
     *
     * Ce champ contient le mot de passe de l'utilisateur après l'application d'un algorithme de hachage.
     */
    private String mdp;

    private String token;

    private Boolean estClient;
}
