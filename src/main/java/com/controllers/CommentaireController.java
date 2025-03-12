package com.controllers;

import com.dtos.ApiResponse;
import com.dtos.CommentaireDto;
import com.dtos.IngredientDto;
import com.dtos.PizzaDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.CommentaireService;
import com.services.impl.CommentaireServiceImpl;
import com.services.impl.FileStorageServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import jakarta.servlet.http.Cookie;
import org.springframework.web.multipart.MultipartFile;

/**
 * Contrôleur REST pour gérer les commentaires.
 */
@RestController
@RequestMapping("/api/commentaire")
public class CommentaireController {
    private final CommentaireServiceImpl commentaireService;
    private final FileStorageServiceImpl fileStorageService;

    /**
     * Constructeur du contrôleur.
     * @param commentaireService le service de gestion des commentaires
     */
    public CommentaireController(CommentaireServiceImpl commentaireService,FileStorageServiceImpl fileStorageService) {
        this.commentaireService = commentaireService;
        this.fileStorageService = fileStorageService;
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

        ApiResponse<CommentaireDto> res = commentaireService.saveCommentaire(dto);

        if (res.isSuccess() && res.getData() != null) {
            res = commentaireService.getCommentaireById(res.getData().getId());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(res);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CommentaireDto>> savePizza(
            @RequestPart("commentaire") String commentaireJson,
            @RequestPart(value = "image", required = false) MultipartFile file,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        // Logging du JSON reçu
        System.out.println("JSON reçu: " + commentaireJson);

        // Convertir la chaîne JSON en objet DTO
        ObjectMapper objectMapper = new ObjectMapper();
        // Configurer l'ObjectMapper pour être plus tolérant
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        CommentaireDto commentaireDto;
        try {
            commentaireDto = objectMapper.readValue(commentaireJson, CommentaireDto.class);
        } catch (Exception e) {
            e.printStackTrace(); // Afficher la stack trace complète
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur de conversion JSON: " + e.getMessage()));
        }

        // Gérer le fichier image
        System.out.println("Fichier photo: " + file);
        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            commentaireDto.setPhoto(fileName);
        }

        // Sauvegarder la pizza
        ApiResponse<CommentaireDto> res = commentaireService.saveCommentaire(commentaireDto);
        res = commentaireService.getCommentaireById(res.getData().getId());
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(res);
    }


    /**
     * Récupère un commentaire par son ID.
     * @param id l'identifiant du commentaire
     * @return une réponse HTTP contenant le commentaire ou une erreur si introuvable
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentaireDto>> getCommentaireById(@PathVariable Long id) {
        ApiResponse<CommentaireDto> res = commentaireService.getCommentaireById(id);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }

    /**
     * Récupère tous les commentaires d'une pizza.
     * @param idPizza l'identifiant de la pizza
     * @return une réponse HTTP contenant la liste des commentaires associés
     */
    @GetMapping("/pizza/{idPizza}")
    public ResponseEntity<ApiResponse<List<CommentaireDto>>> getCommentairesByPizza(@PathVariable Long idPizza) {
        ApiResponse<List<CommentaireDto>> res = commentaireService.getCommentaireByPizza(idPizza);
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

        ApiResponse<CommentaireDto> res = commentaireService.updateCommentaire(id, dto);
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

        ApiResponse<Void> res = commentaireService.deleteCommentaire(id);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> getCommentaireImage(@PathVariable Long id) {
        try {
            CommentaireDto commentaireDto = commentaireService.getCommentaireById(id).getData();
            if (commentaireDto == null || commentaireDto.getPhoto() == null) {
                return ResponseEntity.notFound().build();
            }

            String imageFileName = commentaireDto.getPhoto();
            Path filePath = fileStorageService.getFilePath(imageFileName);

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException ex) {
            return ResponseEntity.badRequest().build();
        }
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
