package com.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaCommandeDto {
    private Long id;
    private Long pizzaId;
    private Long panierId;
    private int quantite;
    private Long commandeId;
}
