package com.services;

import com.dtos.ApiResponse;
import com.dtos.PanierDto;

public interface PanierService {
    ApiResponse<PanierDto> createPanier(Long userId);
    ApiResponse<PanierDto> getPanierByUserId(Long userId);
    ApiResponse<Void> deletePanier(Long panierId);
}
