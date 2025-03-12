package com.controllers;

import com.services.IngredientService;
import com.services.PanierService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour gérer les commentaires.
 */
@RestController
@RequestMapping("/api/panier")
public class PanierController {
    private final PanierService panierService;

    public PanierController(PanierService panierService) {
        this.panierService = panierService;
    }
}
