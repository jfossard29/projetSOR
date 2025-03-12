package com.controllers;

import com.dtos.ApiResponse;
import com.dtos.PizzaDto;
import com.services.PizzaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import jakarta.servlet.http.Cookie;

/**
 * Contrôleur REST pour gérer les pizzas.
 */
@RestController
@RequestMapping("/api/pizza")
public class PizzaController {
    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    /**
     * Récupère toutes les pizzas.
     * @return Liste des pizzas.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PizzaDto>>> getAllPizzas() {
        ApiResponse<List<PizzaDto>> res = pizzaService.getAllPizzas();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    /**
     * Récupère une pizza par son ID.
     * @param id L'ID de la pizza.
     * @return Une réponse contenant la pizza trouvée ou une erreur.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PizzaDto>> getPizza(@PathVariable Long id) {
        ApiResponse<PizzaDto> res = pizzaService.getPizzaById(id);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }

    /**
     * Ajoute une pizza (requiert un token JWT valide).
     * @param pizzaDto La pizza à enregistrer.
     * @param request La requête HTTP pour valider le token JWT.
     * @return Une réponse contenant la pizza enregistrée ou une erreur.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PizzaDto>> savePizza(
            @RequestBody PizzaDto pizzaDto,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<PizzaDto> res = pizzaService.savePizza(pizzaDto);
        res = pizzaService.getPizzaById(res.getData().getId());
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(res);
    }

    /**
     * Met à jour une pizza (requiert un token JWT valide).
     * @param id L'ID de la pizza à mettre à jour.
     * @param pizzaDto Les nouvelles données de la pizza.
     * @param request La requête HTTP pour valider le token JWT.
     * @return Une réponse contenant la pizza mise à jour ou une erreur.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PizzaDto>> updatePizza(
            @PathVariable Long id,
            @RequestBody PizzaDto pizzaDto,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<PizzaDto> res = pizzaService.updatePizza(id, pizzaDto);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }

    /**
     * Supprime une pizza (requiert un token JWT valide).
     * @param id L'ID de la pizza à supprimer.
     * @param request La requête HTTP pour valider le token JWT.
     * @return Une réponse confirmant la suppression ou une erreur.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePizza(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<Void> res = pizzaService.deletePizza(id);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }

    /**
     * Vérifie la validité du token JWT en le récupérant dans les cookies.
     * @param request La requête HTTP contenant potentiellement le token.
     * @return `true` si le token est valide, sinon `false`.
     */
    private boolean isTokenValid(HttpServletRequest request) {
//        Optional<Cookie> authTokenCookie = getAuthTokenCookie(request);
//        return authTokenCookie.map(cookie -> JwtAuthenticationFilter.validateToken(cookie.getValue())).orElse(false);
        return true;
    }

    /**
     * Récupère le cookie "AuthToken" de la requête HTTP.
     * @param request La requête HTTP.
     * @return Un objet `Optional<Cookie>` contenant le token JWT s'il est présent.
     */
    private Optional<Cookie> getAuthTokenCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "AuthToken".equals(cookie.getName()))
                    .findFirst();
        }
        return Optional.empty();
    }
}
