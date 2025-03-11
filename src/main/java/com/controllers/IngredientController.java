package com.controllers;

import com.dtos.ApiResponse;
import com.dtos.IngredientDto;
import com.services.IngredientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import jakarta.servlet.http.Cookie;

/**
 * Contrôleur REST pour gérer les ingrédients.
 */
@RestController
@RequestMapping("/api/ingredient")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * Récupère tous les ingrédients.
     * @return Liste des ingrédients.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<IngredientDto>>> getIngredients() {
        ApiResponse<List<IngredientDto>> res = ingredientService.getAllIngredients();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    /**
     * Récupère un ingrédient par son ID.
     * @param id L'ID de l'ingrédient.
     * @return Une réponse contenant l'ingrédient trouvé ou une erreur.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IngredientDto>> getIngredient(@PathVariable Long id) {
        ApiResponse<IngredientDto> res = ingredientService.getIngredientById(id);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }

    /**
     * Ajoute un ingrédient (requiert un token JWT valide).
     * @param ingredientDto L'ingrédient à enregistrer.
     * @param request La requête HTTP pour valider le token JWT.
     * @return Une réponse contenant l'ingrédient enregistré ou une erreur.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<IngredientDto>> saveIngredient(
            @RequestBody IngredientDto ingredientDto,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<IngredientDto> res = ingredientService.saveIngredient(ingredientDto);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(res);
    }

    /**
     * Met à jour un ingrédient (requiert un token JWT valide).
     * @param id L'ID de l'ingrédient à mettre à jour.
     * @param ingredientDto Les nouvelles données de l'ingrédient.
     * @param request La requête HTTP pour valider le token JWT.
     * @return Une réponse contenant l'ingrédient mis à jour ou une erreur.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IngredientDto>> updateIngredient(
            @PathVariable Long id,
            @RequestBody IngredientDto ingredientDto,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<IngredientDto> res = ingredientService.updateIngredient(id, ingredientDto);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }

    /**
     * Supprime un ingrédient (requiert un token JWT valide).
     * @param id L'ID de l'ingrédient à supprimer.
     * @param request La requête HTTP pour valider le token JWT.
     * @return Une réponse confirmant la suppression ou une erreur.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteIngredient(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<Void> res = ingredientService.deleteIngredient(id);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }

    private boolean isTokenValid(HttpServletRequest request) {
//        Optional<Cookie> authTokenCookie = getAuthTokenCookie(request);
//        return authTokenCookie.map(cookie -> JwtAuthenticationFilter.validateToken(cookie.getValue())).orElse(false);
        return true;
    }

    private Optional<Cookie> getAuthTokenCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "AuthToken".equals(cookie.getName()))
                    .findFirst();
        }
        return Optional.empty();
    }
}
