package com.controllers;

import com.dtos.ApiResponse;
import com.dtos.CommentaireDto;
import com.services.CommentaireService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import jakarta.servlet.http.Cookie;

/**
 * Contrôleur REST pour gérer les commentaires.
 */
@RestController
@RequestMapping("/api/commentaire")
public class CommentaireController {
    private final CommentaireService service;

    /**
     * Constructeur du contrôleur.
     * @param service le service de gestion des commentaires
     */
    public CommentaireController(CommentaireService service) {
        this.service = service;
    }

    /**
     * Ajoute un commentaire (requiert un token JWT valide).
     * @param dto le commentaire à enregistrer
     * @param request la requête HTTP pour valider le token JWT
     * @return une réponse HTTP contenant le commentaire enregistré ou une erreur
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CommentaireDto>> addCommentaire(
            @RequestBody CommentaireDto dto,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<CommentaireDto> res = service.saveCommentaire(dto);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(res);
    }

    /**
     * Récupère un commentaire par son ID.
     * @param id l'identifiant du commentaire
     * @return une réponse HTTP contenant le commentaire ou une erreur si introuvable
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentaireDto>> getCommentaireById(@PathVariable Long id) {
        ApiResponse<CommentaireDto> res = service.getCommentaireById(id);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }

    /**
     * Récupère tous les commentaires d'une pizza.
     * @param idPizza l'identifiant de la pizza
     * @return une réponse HTTP contenant la liste des commentaires associés
     */
    @GetMapping("/pizza/{idPizza}")
    public ResponseEntity<ApiResponse<List<CommentaireDto>>> getCommentairesByPizza(@PathVariable Long idPizza) {
        ApiResponse<List<CommentaireDto>> res = service.getCommentaireByPizza(idPizza);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }


    /**
     * Met à jour un commentaire (requiert un token JWT valide).
     * @param id l'identifiant du commentaire à mettre à jour
     * @param dto les nouvelles données du commentaire
     * @param request la requête HTTP pour valider le token JWT
     * @return une réponse HTTP contenant le commentaire mis à jour ou une erreur si introuvable
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentaireDto>> updateCommentaire(
            @PathVariable Long id,
            @RequestBody CommentaireDto dto,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<CommentaireDto> res = service.updateCommentaire(id, dto);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }

    /**
     * Supprime un commentaire (requiert un token JWT valide).
     * @param id l'identifiant du commentaire à supprimer
     * @param request la requête HTTP pour valider le token JWT
     * @return une réponse HTTP indiquant le succès ou l'échec de la suppression
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCommentaire(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<Void> res = service.deleteCommentaire(id);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }

    /**
     * Vérifie la validité du token JWT en le récupérant dans les cookies.
     * @param request la requête HTTP contenant potentiellement le token
     * @return `true` si le token est valide, sinon `false`
     */
    private boolean isTokenValid(HttpServletRequest request) {
        //Optional<Cookie> authTokenCookie = getAuthTokenCookie(request);
        //TODO faire gestion Token
        //return authTokenCookie.map(cookie -> JwtAuthenticationFilter.validateToken(cookie.getValue())).orElse(false);
        return true;
    }

    /**
     * Récupère le cookie "AuthToken" de la requête HTTP.
     * @param request la requête HTTP
     * @return un objet `Optional<Cookie>` contenant le token JWT s'il est présent
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
