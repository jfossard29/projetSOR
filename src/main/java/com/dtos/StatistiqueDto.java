package com.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatistiqueDto {
    private Map<String, Long> pizzas;
    private Map<String, Long> ingredients;
    private Map<String, Long> utilisateurs;
}
