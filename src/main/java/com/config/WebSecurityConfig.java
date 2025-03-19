package com.config;

import com.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * Configuration de la sécurité de l'application.
 */
@Configuration
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructeur injectant le filtre JWT.
     *
     * @param jwtAuthenticationFilter filtre d'authentification JWT
     */
    public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Définit la chaîne de filtres de sécurité.
     *
     * @param http instance de {@link HttpSecurity} pour configurer la sécurité
     * @return une instance de {@link SecurityFilterChain}
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Routes d'authentification: toutes méthodes autorisées
                        .requestMatchers("/api/login/**", "/api/register/**").permitAll()

                                // Pour pizza
                        .requestMatchers(HttpMethod.GET, "/api/pizza/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/pizza/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/pizza/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/pizza/**").authenticated()

                                // Pour pizzaCommande
                        .requestMatchers(HttpMethod.GET, "/api/pizzaCommande/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/pizzaCommande/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/pizzaCommande/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/pizzaCommande/**").permitAll()

                                // Pour ingredient
                        .requestMatchers(HttpMethod.GET, "/api/ingredient/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/ingredient/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/ingredient/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/ingredient/**").authenticated()

                                // Pour commentaire
                        .requestMatchers(HttpMethod.GET, "/api/commentaire/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/commentaire/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/commentaire/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/commentaire/**").authenticated()

                        // Panier: toutes méthodes autorisées
                        .requestMatchers("/api/panier/**").permitAll()
                        .requestMatchers("/api/panier/user/**").permitAll()
                        .requestMatchers("/api/panier/fusion-cookie/**").permitAll()

                        // Toutes les autres routes nécessitent une authentification
                        .anyRequest().authenticated()

                        //TODO : les autres.
                        /* on met /** au cas où le problème de l'id et des images surviendrait de nouveau
                           Je ne pense pas que le filtre va jusqu'à regarder si quelqu'un est un admin pour pouvoir post ou autre.
                           Mais c'est déjà un minimum de sécurité établi.
                         */



                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Définit l'encodeur de mots de passe.
     *
     * @return une instance de {@link BCryptPasswordEncoder}
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}