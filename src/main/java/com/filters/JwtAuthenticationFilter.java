package com.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends org.springframework.web.filter.OncePerRequestFilter {

    /**
     * La clé secrète utilisée pour valider le token JWT.
     * Elle doit être la même que celle utilisée pour générer les tokens.
     */
    private final String SECRET_KEY = "SeCrEtPiZZACeBoN";

    /**
     * Méthode principale du filtre qui intercepte les requêtes HTTP pour vérifier l'authentification via un token JWT.
     * Si le token est valide, l'utilisateur est authentifié dans le contexte de sécurité.
     *
     * @param request  La requête HTTP.
     * @param response La réponse HTTP.
     * @param chain    La chaîne de filtres pour continuer le traitement.
     * @throws ServletException Si une erreur de servlet se produit.
     * @throws IOException      Si une erreur d'entrée/sortie se produit.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Récupération du cookie "AuthToken"
        Optional<Cookie> authTokenCookie = getAuthTokenCookie(request);

        // Si aucun cookie n'est trouvé, on continue sans authentification
        if (authTokenCookie.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        // Validation du token et extraction du nom d'utilisateur
        String token = authTokenCookie.get().getValue();

        try {
            DecodedJWT decodedJWT = validateToken(token);

            // Décoder le token et enregistrer l'utilisateur dans le SecurityContext
            String username = decodedJWT.getSubject();

             var authentication = new UsernamePasswordAuthenticationToken(
                    username, null, null // Mettez des rôles ici si nécessaires
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // Si le token est invalide ou expiré, on renvoie une erreur 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token invalide ou expiré.");
            return;
        }

        // Continuer avec la chaîne de filtres
        chain.doFilter(request, response);
    }

    /**
     * Récupère le cookie "AuthToken" de la requête HTTP.
     *
     * @param request La requête HTTP.
     * @return Un objet Optional contenant le cookie "AuthToken", s'il existe.
     */
    private Optional<Cookie> getAuthTokenCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "AuthToken".equals(cookie.getName()))
                    .findFirst();
        }
        return Optional.empty();
    }

    /**
     * Valide le token JWT en le vérifiant avec la clé secrète.
     *
     * @param token Le token JWT à valider.
     * @return Le JWT décodé si la validation est réussie.
     * @throws Exception Si le token est invalide ou expiré.
     */
    private DecodedJWT validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.require(algorithm).build().verify(token);
    }
}
