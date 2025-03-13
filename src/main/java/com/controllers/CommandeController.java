package com.controllers;

import com.dtos.ApiResponse;
import com.dtos.CommandeDto;
import com.services.impl.CommandeServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/commande")
public class CommandeController {

    private final CommandeServiceImpl commandeService;

    public CommandeController(CommandeServiceImpl commandeService) {
        this.commandeService = commandeService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommandeDto>> addCommande(
            @RequestBody CommandeDto commandeDto,
            HttpServletRequest request) {

        if (!isTokenValid(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Accès non autorisé. Veuillez vous connecter."));
        }

        ApiResponse<CommandeDto> response = commandeService.addCommande(commandeDto);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommandeDto>> getCommandeById(@PathVariable Long id) {
        ApiResponse<CommandeDto> response = commandeService.getCommandeById(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<ApiResponse<Collection<CommandeDto>>> getCommandeByIdUser(@PathVariable Long idUser) {
        ApiResponse<Collection<CommandeDto>> response = commandeService.getCommandeByIdUser(idUser);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/numero/{numeroCommande}")
    public ResponseEntity<ApiResponse<Collection<CommandeDto>>> getCommandeByNumero(@PathVariable String numeroCommande) {
        ApiResponse<Collection<CommandeDto>> response = commandeService.getCommandeByNumero(numeroCommande);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }

    private boolean isTokenValid(HttpServletRequest request) {
        // Implémentation pour vérifier le token JWT (par exemple, en extrayant et en validant un token dans les en-têtes)
        return true; // Placeholder pour validation
    }
}
