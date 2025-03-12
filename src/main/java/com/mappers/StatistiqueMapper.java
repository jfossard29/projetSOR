package com.mappers;

import com.dtos.StatistiqueDto;
import com.entities.Statistique;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StatistiqueMapper {

    public StatistiqueDto toDto(Statistique statistique) {
        if (statistique == null) {
            return null;
        }

        StatistiqueDto statistiqueDto = new StatistiqueDto();

        statistiqueDto.setPizzas(statistique.getPizzas());
        statistiqueDto.setIngredients(statistique.getIngredients());
        statistiqueDto.setUtilisateurs(statistique.getUtilisateurs());

        return statistiqueDto;
    }

    public Statistique toEntity(StatistiqueDto statistiqueDto) {
        if (statistiqueDto == null) {
            return null;
        }

        Statistique statistique = new Statistique();

        statistique.setPizzas(statistiqueDto.getPizzas());
        statistique.setIngredients(statistiqueDto.getIngredients());
        statistique.setUtilisateurs(statistiqueDto.getUtilisateurs());

        return statistique;
    }
}
