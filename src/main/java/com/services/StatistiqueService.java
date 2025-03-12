package com.services;

import com.dtos.ApiResponse;
import com.dtos.StatistiqueDto;

public interface StatistiqueService {
    ApiResponse<StatistiqueDto> getStatistiques();
    void incrementPizzaStat(String pizzaName);
    void incrementIngredientStat(String ingredientName);
    void incrementUserStat(String userName);
}
