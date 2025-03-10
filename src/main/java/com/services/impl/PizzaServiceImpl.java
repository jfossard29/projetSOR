package com.services.impl;

import com.dtos.PizzaDto;
import com.mappers.PizzaMapper;
import com.repositories.PizzaRepository;
import com.services.PizzaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("PizzaService")
@Transactional
public class PizzaServiceImpl implements PizzaService {

    private final PizzaRepository pizzaRepository;
    private final PizzaMapper pizzaMapper;

    /**
     * Constructeur avec injection des dépendances
     * L'injection par constructeur est préférée à @Autowired car :
     * - Elle rend les dépendances obligatoires
     * - Elle facilite les tests unitaires
     * - Elle permet l'immutabilité
     */
    public PizzaServiceImpl(PizzaRepository pizzaRepository, PizzaMapper pizzaMapper) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaMapper = pizzaMapper;
    }

    /**
     * {@inheritDoc}
     * Cette méthode est transactionnelle par défaut grâce à @Transactional sur la classe
     */
    @Override
    public PizzaDto savePizza(PizzaDto pizzaDto) {
        var pizza = pizzaMapper.toEntity(pizzaDto);

        if (pizza.getIngredients_principaux() != null) {
            pizza.getIngredients_principaux().forEach(ingredientPrincipal -> ingredientPrincipal.setPizza(pizza));
        }
        //TODO : les commentaires

        var savedPizza = pizzaRepository.save(pizza);
        return pizzaMapper.toDto(savedPizza);
    }

    /**
     * {@inheritDoc}
     * Utilisation de la méthode orElseThrow pour une gestion élégante des cas d'erreur
     */
    @Override
    @Transactional(readOnly = true)
    public PizzaDto getPizzaById(Long pizzaId) {
        var pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("La pizza avec l'ID %d n'existe pas", pizzaId)));
        return pizzaMapper.toDto(pizza);
    }

    /**
     * {@inheritDoc}
     * La méthode deleteById ne lève pas d'exception si l'entité n'existe pas
     */
    @Override
    public boolean deletePizza(Long pizzaId) {
        pizzaRepository.deleteById(pizzaId);
        return true;
    }

    /**
     * {@inheritDoc}
     * Utilisation de l'API Stream pour une transformation fonctionnelle des données
     */
    @Override
    @Transactional(readOnly = true)
    public List<PizzaDto> getAllPizzas() {
        return pizzaRepository.findAll().stream()
                .map(pizzaMapper::toDto)
                .toList();
    }

    @Override
    public PizzaDto updatePizza(Long pizzaId, PizzaDto pizzaDto) {
        var existingPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("La pizza avec l'ID %d n'existe pas", pizzaId)));

        existingPizza.setDescription(pizzaDto.getDescription());
        existingPizza.setNom(pizzaDto.getNom());
        existingPizza.setPhoto(pizzaDto.getPhoto());
        existingPizza.setPrix(pizzaDto.getPrix());

        var updatedPizza = pizzaRepository.save(existingPizza);
        return pizzaMapper.toDto(updatedPizza);
    }

}
