package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.PizzaCommandeDto;
import com.entities.*;
import com.mappers.PizzaCommandeMapper;
import com.repositories.IngredientRepository;
import com.repositories.PanierRepository;
import com.repositories.PizzaCommandeRepository;
import com.repositories.PizzaRepository;
import com.services.PizzaCommandeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("PizzaCommandeService")
@Transactional
public class PizzaCommandeServiceImpl implements PizzaCommandeService {

    private final PizzaCommandeRepository pizzaCommandeRepository;
    private final PizzaCommandeMapper pizzaCommandeMapper;
    private final IngredientRepository ingredientRepository;
    private final PizzaRepository pizzaRepository;
    private final PanierRepository panierRepository;

    public PizzaCommandeServiceImpl(PizzaCommandeRepository pizzaCommandeRepository, PizzaCommandeMapper pizzaCommandeMapper, IngredientRepository ingredientRepository, PizzaRepository pizzaRepository, PanierRepository panierRepository) {
        this.pizzaCommandeRepository = pizzaCommandeRepository;
        this.pizzaCommandeMapper = pizzaCommandeMapper;
        this.ingredientRepository = ingredientRepository;
        this.pizzaRepository = pizzaRepository;
        this.panierRepository = panierRepository;
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
        try {
            Optional<PizzaCommande> existingPizzaCommandeOpt = pizzaCommandeRepository.findByPizzaIdAndPanierId(
                    pizzaCommandeDto.getPizzaId(), pizzaCommandeDto.getPanierId()
            );

            if (existingPizzaCommandeOpt.isPresent()) {
                return ApiResponse.error("La pizza est déjà dans le panier");
            }

            Pizza pizza = pizzaRepository.findById(pizzaCommandeDto.getPizzaId())
                    .orElseThrow(() -> new EntityNotFoundException("Pizza non trouvée"));

            Panier panier = panierRepository.findById(pizzaCommandeDto.getPanierId())
                    .orElseThrow(() -> new EntityNotFoundException("Panier non trouvé"));

            PizzaCommande pizzaCommande = new PizzaCommande();
            pizzaCommande.setPizza(pizza);
            pizzaCommande.setPanier(panier);
            pizzaCommande.setQuantite(pizzaCommandeDto.getQuantite());

            if (pizzaCommandeDto.getIngredientsOptionnelsIds() != null && !pizzaCommandeDto.getIngredientsOptionnelsIds().isEmpty()) {
                List<IngredientOptionnel> ingredientsOptionnels = new ArrayList<>();

                for (Long ingredientId : pizzaCommandeDto.getIngredientsOptionnelsIds()) {
                    Ingredient ingredient = ingredientRepository.findById(ingredientId)
                            .orElseThrow(() -> new EntityNotFoundException("Ingrédient non trouvé"));

                    IngredientOptionnel ingredientOptionnel = new IngredientOptionnel();
                    ingredientOptionnel.setIngredient(ingredient);
                    ingredientOptionnel.setPizzaCommande(pizzaCommande);
                    ingredientsOptionnels.add(ingredientOptionnel);
                }

                pizzaCommande.setIngredientsOptionnels(ingredientsOptionnels);
            }

            PizzaCommande savedPizzaCommande = pizzaCommandeRepository.save(pizzaCommande);

            return ApiResponse.success(pizzaCommandeMapper.toDto(savedPizzaCommande), "Pizza ajoutée à la commande avec succès !");
        } catch (Exception e) {
            return ApiResponse.error("Erreur lors de l'ajout de la pizza à la commande: " + e.getMessage());
        }
    }


    @Override
    public ApiResponse<PizzaCommandeDto> updatePizzaCommande(Long pizzaCommandeId, PizzaCommandeDto pizzaCommandeDto) {
        try {
            var existingPizzaCommande = pizzaCommandeRepository.findById(pizzaCommandeId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            String.format("La pizza commandée avec l'ID %d n'existe pas", pizzaCommandeId)));

            existingPizzaCommande.setQuantite(pizzaCommandeDto.getQuantite());

            if (existingPizzaCommande.getIngredientsOptionnels() != null) {
                existingPizzaCommande.getIngredientsOptionnels().clear();
            } else {
                existingPizzaCommande.setIngredientsOptionnels(new ArrayList<>());
            }

            if (pizzaCommandeDto.getIngredientsOptionnelsIds() != null && !pizzaCommandeDto.getIngredientsOptionnelsIds().isEmpty()) {
                for (Long ingredientId : pizzaCommandeDto.getIngredientsOptionnelsIds()) {
                    var ingredient = ingredientRepository.findById(ingredientId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    String.format("L'ingrédient optionnel avec l'ID %d n'existe pas", ingredientId)));

                    IngredientOptionnel ingredientOptionnelEntity = new IngredientOptionnel();
                    ingredientOptionnelEntity.setPizzaCommande(existingPizzaCommande);
                    ingredientOptionnelEntity.setIngredient(ingredient);
                    existingPizzaCommande.getIngredientsOptionnels().add(ingredientOptionnelEntity);
                }
            }

            var updatedPizzaCommande = pizzaCommandeRepository.save(existingPizzaCommande);
            return ApiResponse.success(pizzaCommandeMapper.toDto(updatedPizzaCommande), "Pizza commandée mise à jour.");
        } catch (Exception e) {
            return ApiResponse.error("Erreur lors de la mise à jour de la pizza commandée: " + e.getMessage());
        }
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
