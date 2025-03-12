package com.mappers;

import com.dtos.UserDto;
import com.entities.User;
import org.springframework.stereotype.Component;

/**
 * Mapper responsable de la conversion entre les entités User et les DTOs UserDto.
 * Un mapper permet de séparer la couche de persistance de la couche de présentation.
 *
 * Points clés du pattern Mapper :
 * - Conversion bidirectionnelle entre DTO et Entity
 * - Gestion de la sécurité des objets null (null-safety)
 * - Pas de logique métier, uniquement de la transformation
 */
@Component
public class UserMapper {

    /**
     * Convertit une entité User en DTO UserDto
     * Cette méthode est utilisée pour exposer les données aux clients de l'API.
     *
     * @param user l'entité User à convertir
     * @return un UserDto ou null si l'entité est null
     */
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setNom(user.getNom());
        userDto.setMdp(user.getMdp());
        userDto.setEstClient(user.getEstClient());
        userDto.setAdressePostale(user.getAdressePostale());
        userDto.setAdresseEmail(user.getAdresseEmail());
        return userDto;
    }

    /**
     * Convertit un DTO UserDto en entité User
     * Cette méthode est utilisée pour persister les données reçues des clients.
     *
     * @param userDto le DTO UserDto à convertir
     * @return une entité User ou null si le DTO est null
     */
    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        user.setNom(userDto.getNom());
        user.setEstClient(userDto.getEstClient());
        user.setMdp(userDto.getMdp());
        user.setAdressePostale(userDto.getAdressePostale());
        user.setAdresseEmail(userDto.getAdresseEmail());
        return user;
    }
}
