package com.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Arrays;
import java.util.Set;

@Component
public class AuthFilter implements Filter {

    /**
     * Liste des endpoints accessibles sans authentification.
     * Ces URL ne nécessitent pas de token d'authentification pour être accessibles.
     */
    private static final Set<String> PUBLIC_URLS = new HashSet<>();

    static {
        PUBLIC_URLS.add("/api/login");
        PUBLIC_URLS.add("/api/register");
        PUBLIC_URLS.add("/api/pizza");
        PUBLIC_URLS.add("/api/ingredient");

    }

    private final String SECRET_KEY = "SeCrEtPiZZACeBoN";

    /**
     * Méthode principale du filtre qui intercepte les requêtes HTTP.
     * Vérifie si l'authentification est nécessaire pour l'endpoint demandé.
     *
     * @param request  La requête HTTP.
     * @param response La réponse HTTP.
     * @param chain    La chaîne de filtres pour continuer le traitement.
     * @throws IOException      Si une erreur d'entrée/sortie se produit.
     * @throws ServletException Si une erreur de servlet se produit.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getServletPath();
        System.out.println("Path intercepté par AuthFilter : " + path);

        // Si l'URL est publique, on autorise l'accès sans authentification
        if (PUBLIC_URLS.contains(path)) {
            System.out.println("Accès autorisé sans authentification pour : " + path);
            chain.doFilter(request, response);
            return;
        }

        // Récupération du cookie "AuthToken" pour vérifier l'authentification
        Optional<Cookie> authTokenCookie = getAuthTokenCookie(httpRequest);

        // Si le token est présent et valide, on continue la chaîne de filtres
        if (authTokenCookie.isPresent() && validateToken(authTokenCookie.get().getValue())) {
            try {
                chain.doFilter(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Si le token est invalide, on renvoie un code d'erreur 401
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Accès non autorisé. Veuillez vous connecter.");
        }
    }

    /**
     * Récupère le cookie "AuthToken" de la requête HTTP.
     *
     * @param request La requête HTTP.
     * @return Un objet Optional contenant le cookie "AuthToken", s'il existe.
     */
    private Optional<Cookie> getAuthTokenCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            System.out.println("Cookies reçus : ");
            Arrays.stream(request.getCookies()).forEach(cookie ->
                    System.out.println("Cookie : " + cookie.getName() + " = " + cookie.getValue())
            );
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "AuthToken".equals(cookie.getName()))
                    .findFirst();
        } else {
            System.out.println("Aucun cookie reçu dans la requête.");
        }
        return Optional.empty();
    }

    /**
     * Valide le token JWT en le vérifiant avec la clé secrète.
     *
     * @param token Le token JWT à valider.
     * @return true si le token est valide, false sinon.
     */
    private boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);

            // Affiche les informations du token pour diagnostic
            System.out.println("Token valide : " + token);
            System.out.println("Sujet (username) : " + decodedJWT.getSubject());
            System.out.println("Expiration : " + decodedJWT.getExpiresAt());
            return true;
        } catch (Exception e) {
            System.out.println("Échec de validation du token : " + token + " - Erreur : " + e.getMessage());
            return false;
        }
    }
}
