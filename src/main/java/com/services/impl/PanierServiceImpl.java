package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.CommandeDto;
import com.dtos.PanierDto;
import com.entities.Commande;
import com.entities.Panier;
import com.entities.PizzaCommande;
import com.mappers.CommandeMapper;
import com.mappers.PanierMapper;
import com.repositories.CommandeRepository;
import com.repositories.PanierRepository;
import com.repositories.PizzaCommandeRepository;
import com.repositories.UserRepository;
import com.services.PanierService;
import com.services.StatistiqueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service("PanierService")
@Transactional
public class PanierServiceImpl implements PanierService {

    private final PanierRepository panierRepository;
    private final PizzaCommandeRepository pizzaCommandeRepository;
    private final UserRepository userRepository;
    private final CommandeRepository commandeRepository;
    private final StatistiqueService statistiqueService;
    private final PanierMapper panierMapper;
    private final CommandeMapper commandeMapper;

    public PanierServiceImpl(PanierRepository panierRepository, PizzaCommandeRepository pizzaCommandeRepository,
                             UserRepository userRepository, CommandeRepository commandeRepository,
                             StatistiqueService statistiqueService, PanierMapper panierMapper, CommandeMapper commandeMapper) {
        this.panierRepository = panierRepository;
        this.pizzaCommandeRepository = pizzaCommandeRepository;
        this.userRepository = userRepository;
        this.commandeRepository = commandeRepository;
        this.statistiqueService = statistiqueService;
        this.panierMapper = panierMapper;
        this.commandeMapper = commandeMapper;
    }

    @Override
    public ApiResponse<PanierDto> creerPanier(PanierDto panierDto) {
        var user = userRepository.findById(panierDto.getIdUser());
        if (user.isEmpty()) {
            return ApiResponse.error("Utilisateur introuvable");
        }

        Panier panier = new Panier();
        panier.setUser(user.get());
        panierRepository.save(panier);

        return ApiResponse.success(panierMapper.toDto(panier), "Panier créé avec succès !");
    }

    @Override
    public ApiResponse<PanierDto> viderPanier(Long idPanier) {
        Optional<Panier> panierOpt = panierRepository.findById(idPanier);
        if (panierOpt.isEmpty()) {
            return ApiResponse.error("Panier introuvable");
        }

        pizzaCommandeRepository.deleteAllByPanierId(idPanier);
        return ApiResponse.success(null, "Panier vidé avec succès.");
    }

    @Override
    public ApiResponse<PanierDto> getPanierByIdUser(Long userId) {
        Optional<Panier> panierOpt = panierRepository.findByUserId(userId);
        if (panierOpt.isEmpty()) {
            return ApiResponse.error("Aucun panier trouvé pour cet utilisateur");
        }

        return ApiResponse.success(panierMapper.toDto(panierOpt.get()), "Panier trouvé !");
    }

    @Override
    public ApiResponse<PanierDto> fusionPanier(Long userId, PanierDto panierDto) {
        Optional<Panier> panierOpt = panierRepository.findByUserId(userId);
        if (panierOpt.isEmpty()) {
            return ApiResponse.error("Aucun panier trouvé pour cet utilisateur");
        }

        Panier panier = panierOpt.get();
        for (Long pizzaCommandeId : panierDto.getPizzaCommandeIds()) {
            Optional<PizzaCommande> pizzaCommandeOpt = pizzaCommandeRepository.findById(pizzaCommandeId);
            if (pizzaCommandeOpt.isEmpty()) {
                return ApiResponse.error("PizzaCommande introuvable");
            }
            PizzaCommande pizzaCommande = pizzaCommandeOpt.get();
            pizzaCommande.setPanier(panier);
            pizzaCommandeRepository.save(pizzaCommande);
        }

        return ApiResponse.success(panierMapper.toDto(panier), "Panier fusionné avec succès.");
    }


    @Override
    public ApiResponse<CommandeDto> validerPanier(Long idPanier) {
        Optional<Panier> panierOpt = panierRepository.findById(idPanier);
        if (panierOpt.isEmpty()) {
            return ApiResponse.error("Panier introuvable");
        }

        Panier panier = panierOpt.get();
        if (panier.getPizzaCommandes().isEmpty()) {
            return ApiResponse.error("Le panier est vide, impossible de valider la commande.");
        }

        String numeroCommande = UUID.randomUUID().toString();
        LocalDate date = LocalDate.now();

        for (PizzaCommande pizzaCommande : panier.getPizzaCommandes()) {
            Commande commande = new Commande();
            commande.setNumeroCommande(numeroCommande);
            commande.setDate(date);
            commande.setPizza(pizzaCommande);
            commandeRepository.save(commande);

            // Mise à jour des statistiques
            statistiqueService.incrementPizzaStat(pizzaCommande.getPizza().getNom());
            pizzaCommande.getIngredientsOptionnels().forEach(ingredientOptionnel ->
                    statistiqueService.incrementIngredientStat(ingredientOptionnel.getIngredient().getNom()));
            statistiqueService.incrementUserStat(panier.getUser().getNom());
        }

        pizzaCommandeRepository.deleteAllByPanierId(idPanier);

        return ApiResponse.success(null, "Panier validé et converti en commande.");
    }
}
