package com.services.impl;

import com.dtos.ApiResponse;
import com.dtos.PanierDto;
import com.entities.Panier;
import com.entities.User;
import com.mappers.PanierMapper;
import com.repositories.PanierRepository;
import com.repositories.UserRepository;
import com.services.PanierService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("PanierService")
@Transactional
public class PanierServiceImpl implements PanierService {

    private final PanierRepository panierRepository;
    private final UserRepository userRepository;
    private final PanierMapper panierMapper;

    public PanierServiceImpl(PanierRepository panierRepository, UserRepository userRepository, PanierMapper panierMapper) {
        this.panierRepository = panierRepository;
        this.userRepository = userRepository;
        this.panierMapper = panierMapper;
    }

    @Override
    public ApiResponse<PanierDto> createPanier(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        if (panierRepository.existsByUserId(userId)) {
            return ApiResponse.error("L'utilisateur a déjà un panier existant.");
        }

        Panier panier = new Panier();
        panier.setUser(user);
        panier = panierRepository.save(panier);

        return ApiResponse.success(panierMapper.toDto(panier), "Panier créé avec succès !");
    }

    @Override
    public ApiResponse<PanierDto> getPanierByUserId(Long userId) {
        Panier panier = panierRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Aucun panier trouvé pour cet utilisateur"));

        return ApiResponse.success(panierMapper.toDto(panier), "Panier trouvé !");
    }

    @Override
    public ApiResponse<Void> deletePanier(Long panierId) {
        if (!panierRepository.existsById(panierId)) {
            return ApiResponse.error("Panier introuvable.");
        }
        panierRepository.deleteById(panierId);
        return ApiResponse.success(null, "Panier supprimé avec succès.");
    }
}
