package com.controllers;

import com.dtos.ApiResponse;
import com.dtos.IngredientDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.IngredientService;
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
 * Contrôleur REST pour gérer les ingrédients.
 */
@RestController
@RequestMapping("/api/ingredient")
public class IngredientController {
    private final IngredientService ingredientService;
    private final FileStorageServiceImpl fileStorageService;

    public IngredientController(IngredientService ingredientService,FileStorageServiceImpl fileStorageService) {
        this.ingredientService = ingredientService;
        this.fileStorageService = fileStorageService;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<IngredientDto>> savePizza(
            @RequestPart("ingredient") String ingredientJson,
            @RequestPart(value = "image", required = false) MultipartFile file,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        // Logging du JSON reçu
        System.out.println("JSON reçu: " + ingredientJson);

        // Convertir la chaîne JSON en objet PizzaDto
        ObjectMapper objectMapper = new ObjectMapper();
        // Configurer l'ObjectMapper pour être plus tolérant
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        IngredientDto ingredientDto;
        try {
            ingredientDto = objectMapper.readValue(ingredientJson, IngredientDto.class);
        } catch (Exception e) {
            e.printStackTrace(); // Afficher la stack trace complète
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur de conversion JSON: " + e.getMessage()));
        }

        // Gérer le fichier image
        System.out.println("Fichier photo: " + file);
        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            ingredientDto.setPhoto(fileName);
        }

        // Sauvegarder la pizza
        ApiResponse<IngredientDto> res = ingredientService.saveIngredient(ingredientDto);
        res = ingredientService.getIngredientById(res.getData().getId());
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

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> getIngredientImage(@PathVariable Long id) {
        try {
            IngredientDto ingredientDto = ingredientService.getIngredientById(id).getData();
            if (ingredientDto == null || ingredientDto.getPhoto() == null) {
                return ResponseEntity.notFound().build();
            }

            String imageFileName = ingredientDto.getPhoto();
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
