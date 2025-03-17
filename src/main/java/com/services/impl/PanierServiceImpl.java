package com.services.impl;

import com.dtos.*;
import com.entities.*;
import com.mappers.CommandeMapper;
import com.mappers.PanierMapper;
import com.repositories.*;
import com.services.PanierService;
import com.services.StatistiqueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private final PizzaRepository pizzaRepository;
    private final IngredientRepository ingredientRepository;
    public PanierServiceImpl(PanierRepository panierRepository, PizzaCommandeRepository pizzaCommandeRepository,
                             UserRepository userRepository, CommandeRepository commandeRepository,
                             StatistiqueService statistiqueService, PanierMapper panierMapper, CommandeMapper commandeMapper, PizzaRepository pizzaRepository,IngredientRepository ingredientRepository) {
        this.panierRepository = panierRepository;
        this.pizzaCommandeRepository = pizzaCommandeRepository;
        this.userRepository = userRepository;
        this.commandeRepository = commandeRepository;
        this.statistiqueService = statistiqueService;
        this.panierMapper = panierMapper;
        this.commandeMapper = commandeMapper;
        this.pizzaRepository = pizzaRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public ApiResponse<PanierDto> creerPanier(PanierDto panierDto) {
        // Vérifier si l'utilisateur existe
        var user = userRepository.findById(panierDto.getIdUser());
        if (user.isEmpty()) {
            return ApiResponse.error("Utilisateur introuvable");
        }

        Panier existingPanier = panierRepository.findByUser(user.get());
        if (existingPanier != null) {
            return ApiResponse.error("Un panier existe déjà pour cet utilisateur");
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

    @Transactional
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

        User user = userRepository.findById(panier.getUser().getId()).orElseThrow();
        String numeroCommande = UUID.randomUUID().toString();
        LocalDate date = LocalDate.now();

        // Sauvegarde temporaire des pizzas commandes avant suppression
        List<PizzaCommande> pizzaCommandes = new ArrayList<>(panier.getPizzaCommandes());

        // Supprimer les pizzas commandes du panier
        panier.getPizzaCommandes().clear();
        panierRepository.save(panier); // Mise à jour du panier

        // Supprimer les pizzas commandes en base
        pizzaCommandeRepository.deleteAllByPanierId(idPanier);

        for (PizzaCommande pizzaCommande : pizzaCommandes) {
            Commande commande = new Commande();
            commande.setNumeroCommande(numeroCommande);
            commande.setDate(date);
            commande.setPizza(pizzaRepository.findById(pizzaCommande.getPizza().getId()).get());
            commande.setUser(user);
            commandeRepository.save(commande);

            // Mise à jour des statistiques
            statistiqueService.incrementPizzaStat(pizzaCommande.getPizza().getNom());

            // Mise à jour des statistiques pour les ingrédients principaux
            pizzaCommande.getPizza().getIngredients_principaux().forEach(ingredientPrincipal ->
                    statistiqueService.incrementIngredientStat(ingredientPrincipal.getIngredient().getNom())
            );

            // Mise à jour des statistiques pour les ingrédients optionnels
            pizzaCommande.getIngredientsOptionnels().forEach(ingredientOptionnel ->
                    statistiqueService.incrementIngredientStat(ingredientOptionnel.getIngredient().getNom())
            );

            // Mise à jour des statistiques pour l'utilisateur
            statistiqueService.incrementUserStat(panier.getUser().getNom());
        }

        return ApiResponse.success(null, "Panier validé et converti en commande.");
    }

    @Override
    @Transactional
    public ApiResponse<PanierDto> fusionPanierCookie(Long userId, PanierFusionDto panierFusionDto) {
        Optional<Panier> panierOpt = panierRepository.findByUserId(userId);
        if (panierOpt.isEmpty()) {
            return ApiResponse.error("Aucun panier trouvé pour cet utilisateur");
        }

        Panier panier = panierOpt.get();

        // Traiter chaque pizza commande du cookie
        for (PizzaCommandeCookieDto pizzaCommandeCookieDto : panierFusionDto.getPizzaCommandes()) {
            try {
                // Récupérer la pizza
                Optional<Pizza> pizzaOpt = pizzaRepository.findById(pizzaCommandeCookieDto.getPizzaId());
                if (pizzaOpt.isEmpty()) {
                    continue; // Ignorer cette pizza si elle n'existe pas
                }

                // Créer la pizza commande
                PizzaCommande pizzaCommande = new PizzaCommande();
                pizzaCommande.setPizza(pizzaOpt.get());
                pizzaCommande.setQuantite(pizzaCommandeCookieDto.getQuantite());
                pizzaCommande.setPanier(panier);

                // Ajouter les ingrédients optionnels
                if (pizzaCommandeCookieDto.getIngredientsOptionnelsIds() != null && !pizzaCommandeCookieDto.getIngredientsOptionnelsIds().isEmpty()) {
                    for (Long ingredientId : pizzaCommandeCookieDto.getIngredientsOptionnelsIds()) {
                        // Récupérer l'ingrédient
                        Optional<Ingredient> ingredientOpt = ingredientRepository.findById(ingredientId);
                        if (ingredientOpt.isPresent()) {
                            // Ajouter l'ingrédient à la pizza commande
                            // Note: Cela dépend de la structure de votre entité PizzaCommande
                            // Vous devrez adapter cette partie selon votre modèle
                            IngredientOptionnel ingredientOptionnel = new IngredientOptionnel();
                            ingredientOptionnel.setIngredient(ingredientOpt.get());
                            ingredientOptionnel.setPizzaCommande(pizzaCommande);
                            pizzaCommande.getIngredientsOptionnels().add(ingredientOptionnel);
                        }
                    }
                }

                // Sauvegarder la pizza commande
                pizzaCommandeRepository.save(pizzaCommande);

            } catch (Exception e) {
                // Log l'erreur mais continuer avec les autres pizzas
                System.err.println("Erreur lors de l'ajout d'une pizza au panier: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return ApiResponse.success(panierMapper.toDto(panier), "Panier fusionné avec succès.");
    }

}
