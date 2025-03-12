package com.services.impl;

import com.dtos.IngredientDto;
import com.dtos.UserDto;
import com.entities.User;
import com.mappers.DogMapper;
import com.mappers.UserMapper;
import com.repositories.DogRepository;
import com.repositories.UserRepository;
import com.services.AuthService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service gérant la logique métier des utilisateurs.
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Getter
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;




    private final String SECRET_KEY = "SeCrEtPiZZACeBoN";

    public AuthServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User registerUser(UserDto userDTO) {
        User user = new User();
        user.setNom(userDTO.getNom());
        user.setMdp(new BCryptPasswordEncoder().encode(userDTO.getMdp()));
        user.setEstClient(userDTO.getEstClient());
        user.setAdresseEmail(userDTO.getAdresseEmail());
        user.setAdressePostale(userDTO.getAdressePostale());
        return userRepository.save(user);
    }

    @PostConstruct
    public void seedAdminUser() {
        String adminUsername = "admin";
        Boolean estClient = false;

        // Vérification si l'utilisateur "admin" existe déjà
        if (userRepository.findByNom(adminUsername).isEmpty()) {
            User adminUser = new User();
            adminUser.setNom(adminUsername);
            adminUser.setMdp(passwordEncoder.encode("lespizzas"));
            adminUser.setAdresseEmail("papa_louis@services.com");
            adminUser.setEstClient(estClient);

            userRepository.save(adminUser);
            System.out.println("Utilisateur 'admin' créé avec succès.");
        } else {
            System.out.println("L'utilisateur 'admin' existe déjà, aucune action nécessaire.");
        }
    }
    @Override
    public UserDto getUserById(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Le compte avec l'ID %d n'existe pas", userId)));
        return userMapper.toDto(user);
    }
    @Override
    public boolean deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return true;
    }
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        var existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Le compte avec l'ID %d n'existe pas", userId)));

        existingUser.setNom(userDto.getNom());

        if (userDto.getMdp() != null && !userDto.getMdp().isBlank()) {
            existingUser.setMdp(passwordEncoder.encode(userDto.getMdp()));
        }

        existingUser.setAdressePostale(userDto.getAdressePostale());
        existingUser.setAdresseEmail(userDto.getAdresseEmail());

        var updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

}


