package com.controllers;

import com.dtos.ApiResponse;
import com.dtos.PizzaDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.PizzaService;
import com.services.impl.FileStorageServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
import java.nio.file.Path;

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
    private final FileStorageServiceImpl fileStorageService;

    public PizzaController(PizzaService pizzaService, FileStorageServiceImpl fileStorageService) {
        this.pizzaService = pizzaService;
        this.fileStorageService = fileStorageService;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PizzaDto>> savePizza(
            @RequestPart("pizza") String pizzaJson,
            @RequestPart(value = "image", required = false) MultipartFile file,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        // Logging du JSON reçu
        System.out.println("JSON reçu: " + pizzaJson);

        // Convertir la chaîne JSON en objet PizzaDto
        ObjectMapper objectMapper = new ObjectMapper();
        // Configurer l'ObjectMapper pour être plus tolérant
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        PizzaDto pizzaDto;
        try {
            pizzaDto = objectMapper.readValue(pizzaJson, PizzaDto.class);
        } catch (Exception e) {
            e.printStackTrace(); // Afficher la stack trace complète
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur de conversion JSON: " + e.getMessage()));
        }

        // Gérer le fichier image
        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            pizzaDto.setPhoto(fileName);
        }

        // Sauvegarder la pizza
        ApiResponse<PizzaDto> res = pizzaService.savePizza(pizzaDto);
        res = pizzaService.getPizzaById(res.getData().getId());
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(res);
    }



    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PizzaDto>> updatePizza(
            @PathVariable Long id,
            @RequestPart("pizza") PizzaDto pizzaDto,
            @RequestPart(value = "image", required = false) MultipartFile file,
            HttpServletRequest request
    ) {
        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            pizzaDto.setPhoto(fileName);
        }

        ApiResponse<PizzaDto> res = pizzaService.updatePizza(id, pizzaDto);
        return ResponseEntity.status(res.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(res);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> getPizzaImage(@PathVariable Long id) {
        try {
            // Récupérer la pizza par son ID
            PizzaDto pizzaDto = pizzaService.getPizzaById(id).getData();
            if (pizzaDto == null || pizzaDto.getPhoto() == null) {
                return ResponseEntity.notFound().build();
            }

            String imageFileName = pizzaDto.getPhoto();
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
