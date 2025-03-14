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

    private Long id;

    private String nom;

    private String mdp;

    private String adressePostale;

    private String adresseEmail;

    private Boolean estClient;
}
