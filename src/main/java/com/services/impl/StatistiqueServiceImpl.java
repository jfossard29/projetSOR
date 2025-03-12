package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.StatistiqueDto;
import com.entities.Statistique;
import com.mappers.StatistiqueMapper;
import com.repositories.StatistiqueRepository;
import com.services.StatistiqueService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StatistiqueServiceImpl implements StatistiqueService {

    private final StatistiqueRepository repository;
    private final StatistiqueMapper mapper;

    public StatistiqueServiceImpl(StatistiqueRepository repository, StatistiqueMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ApiResponse<StatistiqueDto> getStatistiques() {
        Statistique statistique = repository.findAll().stream().findFirst().orElse(null);
        return ApiResponse.success(mapper.toDto(statistique),"Statistique");
    }

    @Override
    public void incrementPizzaStat(String pizzaName) {
        Statistique statistique = repository.findAll().stream().findFirst().orElse(new Statistique());
        Map<String, Long> pizzas = statistique.getPizzas();
        pizzas.put(pizzaName, pizzas.getOrDefault(pizzaName, 0L) + 1);
        statistique.setPizzas(pizzas);
        repository.save(statistique);
    }

    @Override
    public void incrementIngredientStat(String ingredientName) {
        Statistique statistique = repository.findAll().stream().findFirst().orElse(new Statistique());
        Map<String, Long> ingredients = statistique.getIngredients();
        ingredients.put(ingredientName, ingredients.getOrDefault(ingredientName, 0L) + 1);
        statistique.setIngredients(ingredients);
        repository.save(statistique);
    }

    @Override
    public void incrementUserStat(String userName) {
        Statistique statistique = repository.findAll().stream().findFirst().orElse(new Statistique());
        Map<String, Long> utilisateurs = statistique.getUtilisateurs();
        utilisateurs.put(userName, utilisateurs.getOrDefault(userName, 0L) + 1);
        statistique.setUtilisateurs(utilisateurs);
        repository.save(statistique);
    }
}
