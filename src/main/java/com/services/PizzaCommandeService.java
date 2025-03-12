package com.services;

import com.dtos.ApiResponse;
import com.dtos.PizzaCommandeDto;

public interface PizzaCommandeService {
    ApiResponse<PizzaCommandeDto> addPizzaToPanier(Long panierId, Long pizzaId, int quantite);
    ApiResponse<PizzaCommandeDto> getPizzaCommandeById(Long id);
    ApiResponse<Void> deletePizzaCommande(Long id);
}
