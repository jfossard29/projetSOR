package com.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaCommandeCookieDto {
    private Long pizzaId;
    private int quantite;
    private List<Long> ingredientsOptionnelsIds;
}