package com.services;

import com.dtos.UserDto;
import com.entities.User;

/**
 * Interface gérant la logique métier des utilisateurs.
 */
public interface AuthService {


    User registerUser(UserDto userDTO);

    void seedAdminUser();

}

