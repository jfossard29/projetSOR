package com.services.impl;

import com.dtos.IngredientDto;
import com.mappers.IngredientMapper;
import com.repositories.IngredientRepository;
import com.services.IngredientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("IngredientService")
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    /**
     * Constructeur avec injection des dépendances
     * L'injection par constructeur est préférée à @Autowired car :
     * - Elle rend les dépendances obligatoires
     * - Elle facilite les tests unitaires
     * - Elle permet l'immutabilité
     */
    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    /**
     * {@inheritDoc}
     * Cette méthode est transactionnelle par défaut grâce à @Transactional sur la classe
     */
    @Override
    public IngredientDto saveIngredient(IngredientDto ingredientDto) {
        var ingredient = ingredientMapper.toEntity(ingredientDto);
        var savedIngredient = ingredientRepository.save(ingredient);
        return ingredientMapper.toDto(savedIngredient);
    }

    /**
     * {@inheritDoc}
     * Utilisation de la méthode orElseThrow pour une gestion élégante des cas d'erreur
     */
    @Override
    @Transactional(readOnly = true)
    public IngredientDto getIngredientById(Long ingredientId) {
        var ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("L'ingrédient avec l'ID %d n'existe pas", ingredientId)));
        return ingredientMapper.toDto(ingredient);
    }

    /**
     * {@inheritDoc}
     * La méthode deleteById ne lève pas d'exception si l'entité n'existe pas
     */
    @Override
    public boolean deleteIngredient(Long ingredientId) {
        ingredientRepository.deleteById(ingredientId);
        return true;
    }

    /**
     * {@inheritDoc}
     * Utilisation de l'API Stream pour une transformation fonctionnelle des données
     */
    @Override
    @Transactional(readOnly = true)
    public List<IngredientDto> getAllIngredients() {
        return ingredientRepository.findAll().stream()
                .map(ingredientMapper::toDto)
                .toList();
    }

    @Override
    public IngredientDto updateIngredient(Long ingredientId, IngredientDto ingredientDto) {
        var existingIngredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("L'ingrédient avec l'ID %d n'existe pas", ingredientId)));

        existingIngredient.setDescription(ingredientDto.getDescription());
        existingIngredient.setNom(ingredientDto.getNom());
        existingIngredient.setPhoto(ingredientDto.getPhoto());
        existingIngredient.setPrix(ingredientDto.getPrix());

        var updatedIngredient = ingredientRepository.save(existingIngredient);
        return ingredientMapper.toDto(updatedIngredient);
    }

}
