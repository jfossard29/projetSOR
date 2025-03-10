package com.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dtos.DogDto;
import com.dtos.IngredientDto;
import com.dtos.UserDto;
import com.entities.User;
import com.repositories.IngredientRepository;
import com.services.AuthService;
import com.services.IngredientService;
import com.services.impl.DogServiceImpl;
import com.services.impl.IngredientServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import com.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur gérant l'authentification des utilisateurs.
 */
@RestController
@RequestMapping("/api")
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    public IngredientController(IngredientServiceImpl ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<IngredientDto> getIngredient() {
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/{id}")
    public IngredientDto getIngredient(@PathVariable Long id){
        return ingredientService.getIngredientById(id);
    }

    @PostMapping
    public IngredientDto saveDog(final @RequestBody IngredientDto ingredientDto){
        return ingredientService.saveIngredient(ingredientDto);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteIngredient(@PathVariable Long id){
        return ingredientService.deleteIngredient(id);
    }
}