package com.services;

import com.dtos.DogDto;
import com.dtos.IngredientDto;
import com.dtos.UserDto;
import com.entities.User;

import java.util.List;

/**
 * Interface gérant la logique métier des utilisateurs.
 */
public interface AuthService {


    User registerUser(UserDto userDTO);

    void seedAdminUser();

    UserDto getUserById(Long userId);

    boolean deleteUser(Long userId);

    List<UserDto> getAllUsers();

    UserDto updateUser(Long userId, UserDto userDto);
}

