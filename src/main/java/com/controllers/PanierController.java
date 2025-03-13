package com.controllers;

import com.dtos.ApiResponse;
import com.dtos.CommandeDto;
import com.dtos.PanierDto;
import com.services.impl.PanierServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/panier")
public class PanierController {

    private final PanierServiceImpl panierService;

    public PanierController(PanierServiceImpl panierService) {
        this.panierService = panierService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PanierDto>> creerPanier(
            @RequestBody PanierDto panierDto,
            HttpServletRequest request) {

        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<PanierDto> response = panierService.creerPanier(panierDto);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PanierDto>> getPanierByIdUser(@PathVariable Long userId) {
        ApiResponse<PanierDto> response = panierService.getPanierByIdUser(userId);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }

    @PostMapping("/fusion/{userId}")
    public ResponseEntity<ApiResponse<PanierDto>> fusionPanier(
            @PathVariable Long userId, @RequestBody PanierDto panierDto, HttpServletRequest request) {

        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<PanierDto> response = panierService.fusionPanier(userId, panierDto);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/vider/{idPanier}")
    public ResponseEntity<ApiResponse<PanierDto>> viderPanier(@PathVariable Long idPanier) {
        ApiResponse<PanierDto> response = panierService.viderPanier(idPanier);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }

    @PostMapping("/valider/{idPanier}")
    public ResponseEntity<ApiResponse<CommandeDto>> validerPanier(@PathVariable Long idPanier) {
        ApiResponse<CommandeDto> response = panierService.validerPanier(idPanier);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    private boolean isTokenValid(HttpServletRequest request) {
        return true;
    }
}
