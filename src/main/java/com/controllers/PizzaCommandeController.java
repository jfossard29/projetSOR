package com.controllers;

import com.dtos.ApiResponse;
import com.dtos.PizzaCommandeDto;
import com.services.impl.PizzaCommandeServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pizzaCommande")
public class PizzaCommandeController {

    private final PizzaCommandeServiceImpl pizzaCommandeService;

    public PizzaCommandeController(PizzaCommandeServiceImpl pizzaCommandeService) {
        this.pizzaCommandeService = pizzaCommandeService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PizzaCommandeDto>> addPizzaCommande(
            @RequestBody PizzaCommandeDto pizzaCommandeDto,
            HttpServletRequest request) {

        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<PizzaCommandeDto> response = pizzaCommandeService.addPizzaCommande(pizzaCommandeDto);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PizzaCommandeDto>> getPizzaCommandeById(@PathVariable Long id) {
        ApiResponse<PizzaCommandeDto> response = pizzaCommandeService.getPizzaCommandeById(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PizzaCommandeDto>> updatePizzaCommande(
            @PathVariable Long id, @RequestBody PizzaCommandeDto pizzaCommandeDto, HttpServletRequest request) {

        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<PizzaCommandeDto> response = pizzaCommandeService.updatePizzaCommande(id, pizzaCommandeDto);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePizzaCommande(@PathVariable Long id) {
        ApiResponse<Void> response = pizzaCommandeService.deletePizzaCommande(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }

    private boolean isTokenValid(HttpServletRequest request) {
        return true; // Placeholder pour validation
    }
}
