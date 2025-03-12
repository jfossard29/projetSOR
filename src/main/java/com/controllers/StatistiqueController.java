package com.controllers;

import com.dtos.ApiResponse;
import com.dtos.StatistiqueDto;
import com.services.StatistiqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistiques")
public class StatistiqueController {

    private final StatistiqueService statistiqueService;

    public StatistiqueController(StatistiqueService statistiqueService) {
        this.statistiqueService = statistiqueService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<StatistiqueDto>> getStatistiques() {
        ApiResponse<StatistiqueDto> statistiques = statistiqueService.getStatistiques();
        return ResponseEntity.status(HttpStatus.OK).body(statistiques);
    }
}
