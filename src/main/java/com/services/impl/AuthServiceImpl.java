package com.services.impl;

import com.dtos.UserDto;
import com.entities.User;
import com.repositories.UserRepository;
import com.services.AuthService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service gérant la logique métier des utilisateurs.
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Getter
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    private final String SECRET_KEY = "secret";

    public User registerUser(UserDto userDTO) {
        User user = new User();
        user.setNom(userDTO.getNom());
        user.setMdp(new BCryptPasswordEncoder().encode(userDTO.getMdp()));
        return userRepository.save(user);
    }

    @PostConstruct
    public void seedAdminUser() {
        String adminUsername = "admin";
        String adminPassword = "lespizzas";

        // Vérification si l'utilisateur "admin" existe déjà
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User adminUser = new User();
            adminUser.setNom(adminUsername);
            adminUser.setNom(passwordEncoder.encode(adminPassword));

            userRepository.save(adminUser);
            System.out.println("Utilisateur 'admin' créé avec succès.");
        } else {
            System.out.println("L'utilisateur 'admin' existe déjà, aucune action nécessaire.");
        }
    }

}


