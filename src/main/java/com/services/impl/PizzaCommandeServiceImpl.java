package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.PizzaCommandeDto;
import com.entities.IngredientOptionnel;
import com.entities.PizzaCommande;
import com.mappers.PizzaCommandeMapper;
import com.repositories.IngredientRepository;
import com.repositories.PizzaCommandeRepository;
import com.services.PizzaCommandeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service("PizzaCommandeService")
@Transactional
public class PizzaCommandeServiceImpl implements PizzaCommandeService {

    private final PizzaCommandeRepository pizzaCommandeRepository;
    private final PizzaCommandeMapper pizzaCommandeMapper;
    private final IngredientRepository ingredientRepository;

    public PizzaCommandeServiceImpl(PizzaCommandeRepository pizzaCommandeRepository, PizzaCommandeMapper pizzaCommandeMapper, IngredientRepository ingredientRepository) {
        this.pizzaCommandeRepository = pizzaCommandeRepository;
        this.pizzaCommandeMapper = pizzaCommandeMapper;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public ApiResponse<PizzaCommandeDto> getPizzaCommandeById(Long id) {
        Optional<PizzaCommande> pizzaCommandeOpt = pizzaCommandeRepository.findById(id);
        if (pizzaCommandeOpt.isEmpty()) {
            return ApiResponse.error("Pizza commandée introuvable");
        }

        return ApiResponse.success(pizzaCommandeMapper.toDto(pizzaCommandeOpt.get()), "Pizza commandée trouvée !");
    }

    @Override
    public ApiResponse<PizzaCommandeDto> addPizzaCommande(PizzaCommandeDto pizzaCommandeDto) {
        Optional<PizzaCommande> existingPizzaCommandeOpt = pizzaCommandeRepository.findByPizzaIdAndPanierId(
                pizzaCommandeDto.getPizzaId(), pizzaCommandeDto.getPanierId()
        );
        if (existingPizzaCommandeOpt.isPresent()) {
            return ApiResponse.error("La pizza est déjà dans le panier");
        }

        PizzaCommande pizzaCommande = pizzaCommandeMapper.toEntity(pizzaCommandeDto);
        pizzaCommandeRepository.save(pizzaCommande);

        return ApiResponse.success(pizzaCommandeMapper.toDto(pizzaCommande), "Pizza ajoutée à la commande avec succès !");
    }

    @Override
    public ApiResponse<PizzaCommandeDto> updatePizzaCommande(Long pizzaCommandeId, PizzaCommandeDto pizzaCommandeDto) {
        var existingPizzaCommande = pizzaCommandeRepository.findById(pizzaCommandeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("La pizza commandée avec l'ID %d n'existe pas", pizzaCommandeId)));

        existingPizzaCommande.setQuantite(pizzaCommandeDto.getQuantite());

        if (existingPizzaCommande.getIngredientsOptionnels() == null) {
            existingPizzaCommande.setIngredientsOptionnels(new ArrayList<>());
        }

        if (pizzaCommandeDto.getIngredientsOptionnelsIds() != null) {
            pizzaCommandeDto.getIngredientsOptionnelsIds().forEach(ingredientId -> {
                var ingredientOptionnel = ingredientRepository.findById(ingredientId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                String.format("L'ingrédient optionnel avec l'ID %d n'existe pas", ingredientId)));

                IngredientOptionnel ingredientOptionnelEntity = new IngredientOptionnel();
                ingredientOptionnelEntity.setPizzaCommande(existingPizzaCommande);
                ingredientOptionnelEntity.setIngredient(ingredientOptionnel);
                existingPizzaCommande.getIngredientsOptionnels().add(ingredientOptionnelEntity);
            });
        }

        var updatedPizzaCommande = pizzaCommandeRepository.save(existingPizzaCommande);
        return ApiResponse.success(pizzaCommandeMapper.toDto(updatedPizzaCommande), "Pizza commandée mise à jour.");
    }

    @Override
    public ApiResponse<Void> deletePizzaCommande(Long id) {
        Optional<PizzaCommande> pizzaCommandeOpt = pizzaCommandeRepository.findById(id);
        if (pizzaCommandeOpt.isEmpty()) {
            return ApiResponse.error("Pizza commandée introuvable");
        }

        pizzaCommandeRepository.deleteById(id);
        return ApiResponse.success(null, "Pizza commandée supprimée avec succès");
    }
}
