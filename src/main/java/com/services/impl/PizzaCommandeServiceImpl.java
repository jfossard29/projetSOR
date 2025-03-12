package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.PizzaCommandeDto;
import com.entities.Panier;
import com.entities.Pizza;
import com.entities.PizzaCommande;
import com.mappers.PizzaCommandeMapper;
import com.repositories.PanierRepository;
import com.repositories.PizzaCommandeRepository;
import com.repositories.PizzaRepository;
import com.services.PizzaCommandeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("PizzaCommandeService")
@Transactional
public class PizzaCommandeServiceImpl implements PizzaCommandeService {

    private final PizzaCommandeRepository pizzaCommandeRepository;
    private final PizzaRepository pizzaRepository;
    private final PanierRepository panierRepository;
    private final PizzaCommandeMapper pizzaCommandeMapper;

    public PizzaCommandeServiceImpl(PizzaCommandeRepository pizzaCommandeRepository, PizzaRepository pizzaRepository,
                                    PanierRepository panierRepository, PizzaCommandeMapper pizzaCommandeMapper) {
        this.pizzaCommandeRepository = pizzaCommandeRepository;
        this.pizzaRepository = pizzaRepository;
        this.panierRepository = panierRepository;
        this.pizzaCommandeMapper = pizzaCommandeMapper;
    }

    @Override
    public ApiResponse<PizzaCommandeDto> addPizzaToPanier(Long panierId, Long pizzaId, int quantite) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new EntityNotFoundException("Panier introuvable"));
        Pizza pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new EntityNotFoundException("Pizza introuvable"));

        PizzaCommande pizzaCommande = new PizzaCommande();
        pizzaCommande.setPizza(pizza);
        pizzaCommande.setPanier(panier);
        pizzaCommande.setQuantite(quantite);

        pizzaCommande = pizzaCommandeRepository.save(pizzaCommande);

        return ApiResponse.success(pizzaCommandeMapper.toDto(pizzaCommande), "Pizza ajoutée au panier !");
    }

    @Override
    public ApiResponse<PizzaCommandeDto> getPizzaCommandeById(Long id) {
        PizzaCommande pizzaCommande = pizzaCommandeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pizza commande introuvable"));
        return ApiResponse.success(pizzaCommandeMapper.toDto(pizzaCommande), "Pizza commande trouvée !");
    }

    @Override
    public ApiResponse<Void> deletePizzaCommande(Long id) {
        if (!pizzaCommandeRepository.existsById(id)) {
            return ApiResponse.error("Pizza commande introuvable.");
        }
        pizzaCommandeRepository.deleteById(id);
        return ApiResponse.success(null, "Pizza supprimée du panier.");
    }
}
