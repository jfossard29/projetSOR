package com.services;

import com.dtos.ApiResponse;
import com.dtos.CommandeDto;

public interface CommandeService {
    ApiResponse<CommandeDto> createCommande(Long pizzaCommandeId);
    ApiResponse<CommandeDto> getCommandeById(Long id);
}
