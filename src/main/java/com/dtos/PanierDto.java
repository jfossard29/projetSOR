package com.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanierDto {
    private Long id;
    private Long idUser;
    private Collection<Long> pizzaCommandeIds;
}
