package com.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeDto {
    private Long id;
    private String numeroCommande;
    private LocalDate date;
    private Long pizzaCommandeId;
}
