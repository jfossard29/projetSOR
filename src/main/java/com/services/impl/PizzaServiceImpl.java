package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.PizzaDto;
import com.entities.IngredientPrincipal;
import com.entities.Pizza;
import com.mappers.PizzaMapper;
import com.repositories.IngredientRepository;
import com.repositories.PizzaRepository;
import com.services.PizzaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("PizzaService")
@Transactional
public class PizzaServiceImpl implements PizzaService {

    private final PizzaRepository pizzaRepository;
    private final PizzaMapper pizzaMapper;
    private  final IngredientRepository ingredientRepository;
    public PizzaServiceImpl(PizzaRepository pizzaRepository, PizzaMapper pizzaMapper, IngredientRepository ingredientRepository) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaMapper = pizzaMapper;
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * Sauvegarde une pizza en base de données.
     * @param pizzaDto La pizza à sauvegarder.
     * @return Une réponse contenant la pizza sauvegardée ou une erreur.
     */
    @Override
    public ApiResponse<PizzaDto> savePizza(PizzaDto pizzaDto) {
        Pizza pizza = pizzaMapper.toEntity(pizzaDto);

        if (pizza.getIngredients_principaux() == null) {
            pizza.setIngredients_principaux(new ArrayList<>());
        }

        final Pizza pizzaFinal = pizza;

        if (pizzaDto.getIngredients_principaux() != null) {
            pizzaDto.getIngredients_principaux().forEach(ingredientDto -> {
                var ingredient = ingredientRepository.findById(ingredientDto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Ingrédient avec ID " + ingredientDto.getId() + " non trouvé"));

                IngredientPrincipal ingredientPrincipal = new IngredientPrincipal();
                ingredientPrincipal.setPizza(pizzaFinal);
                ingredientPrincipal.setIngredient(ingredient);
                pizzaFinal.getIngredients_principaux().add(ingredientPrincipal);
            });
        }

        pizza = pizzaRepository.save(pizza);

        return ApiResponse.success(pizzaMapper.toDto(pizza), "Pizza enregistrée avec succès !");
    }



    /**
     * Récupère une pizza par son ID.
     * @param pizzaId L'ID de la pizza.
     * @return Une réponse contenant la pizza trouvée ou une erreur.
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PizzaDto> getPizzaById(Long pizzaId) {
        var pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("La pizza avec l'ID %d n'existe pas", pizzaId)));

        return ApiResponse.success(pizzaMapper.toDto(pizza), "Pizza trouvée !");
    }

    /**
     * Supprime une pizza par son ID.
     * @param pizzaId L'ID de la pizza.
     * @return Une réponse confirmant la suppression ou une erreur.
     */
    @Override
    public ApiResponse<Void> deletePizza(Long pizzaId) {
        if (!pizzaRepository.existsById(pizzaId)) {
            return ApiResponse.error("La pizza avec l'ID " + pizzaId + " n'existe pas.");
        }

        pizzaRepository.deleteById(pizzaId);
        return ApiResponse.success(null, "Pizza supprimée avec succès.");
    }

    /**
     * Récupère toutes les pizzas.
     * @return Une liste des pizzas existantes.
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<PizzaDto>> getAllPizzas() {
        List<PizzaDto> pizzas = pizzaRepository.findAll().stream()
                .map(pizzaMapper::toDto)
                .toList();

        return ApiResponse.success(pizzas, "Liste des pizzas récupérée avec succès.");
    }

    /**
     * Met à jour une pizza existante.
     * @param pizzaId L'ID de la pizza.
     * @param pizzaDto Les nouvelles informations de la pizza.
     * @return Une réponse contenant la pizza mise à jour ou une erreur.
     */
    @Override
    public ApiResponse<PizzaDto> updatePizza(Long pizzaId, PizzaDto pizzaDto) {
        var existingPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("La pizza avec l'ID %d n'existe pas", pizzaId)));

        existingPizza.setDescription(pizzaDto.getDescription());
        existingPizza.setNom(pizzaDto.getNom());
        existingPizza.setPhoto(pizzaDto.getPhoto());
        existingPizza.setPrix(pizzaDto.getPrix());

        var updatedPizza = pizzaRepository.save(existingPizza);
        return ApiResponse.success(pizzaMapper.toDto(updatedPizza), "Pizza mise à jour.");
    }
}
