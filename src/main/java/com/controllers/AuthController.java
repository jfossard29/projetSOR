package com.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dtos.UserDto;
import com.entities.User;
import com.services.AuthService;
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
class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final String SECRET_KEY = "SeCrEtPiZZACeBoN";
    private static final long EXPIRATION_TIME = 86400000;

    /**
     * Inscription d'un nouvel utilisateur.
     *
     * @param userDTO Informations de l'utilisateur.
     * @return Réponse avec l'utilisateur créé.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDTO) {
        User user = authService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Authentification d'un utilisateur.
     *
     * @param user     Informations d'identification de l'utilisateur.
     * @param response Réponse HTTP pour l'ajout de cookies.
     * @return Réponse contenant le token d'authentification.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletResponse response) {
        Optional<User> optionalUser = userRepository.findByNom(user.getNom());
        if (optionalUser.isEmpty() || !passwordEncoder.matches(user.getMdp(), optionalUser.get().getMdp())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nom d'utilisateur ou mot de passe incorrect");
        }
        String token = generateToken(optionalUser.get());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    /**
     * Déconnexion de l'utilisateur.
     *
     * @return Réponse confirmant la déconnexion.
     */
    @DeleteMapping("/login")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok()
                .header("Set-Cookie", "AuthToken=; HttpOnly; SameSite=strict; Path=/; Max-Age=0")
                .body("Déconnexion réussie !");
    }

    /**
     * Génère un token JWT pour l'utilisateur.
     *
     * @param user Utilisateur concerné.
     * @return Token JWT généré.
     */
    private String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getNom()) // Nom de l'utilisateur
                .withClaim("userId", user.getId()) // ID de l'utilisateur
                .withClaim("estClient", user.getEstClient()) // Champ estClient
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiration
                .sign(Algorithm.HMAC256(SECRET_KEY)); // Signature
    }

    /**
     * Vérifie la validité d'un token.
     *
     * @param bearerToken Token à valider.
     * @return true si le token est valide, false sinon.
     */
    private boolean isTokenValid(String bearerToken) {
        try {
            String token = bearerToken.replace("Bearer ", "");
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Représentation d'une réponse contenant un token.
     *
     * @param token Token JWT.
     */
    private record TokenResponse(String token) {}

    /**
     * Valide un token de session.
     *
     * @return Token décodé.
     * @throws IllegalStateException si la session est invalide.
     */
    /*private DecodedJWT validateSessionToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedToken = verifier.verify(token);
        Optional<User> userSession = userRepository.findByToken(token);
        if (userSession.isEmpty()) {
            throw new IllegalStateException("Session invalide ou expirée !");
        }
        return decodedToken;
    }*/

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        //if(getAuthorization(bearerToken)) {
            return authService.getAllUsers();
        //} else {
        //    return null;
        //}
    }

    /**
     * Récupère les détails d'un utilisateur précis.
     *
     * @param id ID de l'utilisateur.
     * @return Détails de l'utilisateur.
     */
    @GetMapping("/users/{id}")
    public UserDto getUser(@PathVariable Long id) {
        //if(getAuthorization(bearerToken)) {
            return authService.getUserById(id);
       // } else {
        //    return null;
        //}
    }

    /**
     * Supprime un utilisateur avec l'ID spécifié.
     *
     * @param id ID de l'utilisateur à supprimer.
     * @return Confirmation de la suppression.
     */
    @DeleteMapping("/users/{id}")
    public Boolean deleteUser(@PathVariable Long id) {
        //if(getAuthorization(bearerToken)) {
            return authService.deleteUser(id);
       // } else {
       //     return null;
        //}


    }

    /**
     * Met à jour les données d'un utilisateur.
     *
     * @param id      ID de l'utilisateur.
     * @param userDto Données mises à jour au format DTO.
     * @return Données actualisées de l'utilisateur.
     */
    @PutMapping("/users/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
       // if(getAuthorization(bearerToken)) {
            return authService.updateUser(id, userDto);
      //  } else {
       //     return null;
      //  }
    }

    public Boolean getAuthorization(String bearerToken) {
        String token = bearerToken.substring(7);
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
        Boolean estClient = decodedJWT.getClaim("estClient").asBoolean();
        return !estClient;
    }
}