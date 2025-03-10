package com.config;

import com.filters.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration des filtres pour l'application.
 */
@Configuration
public class FilterConfig {

    /**
     * Définit un filtre d'authentification personnalisé.
     *
     * @return une instance de {@link FilterRegistrationBean} configurée avec {@link AuthFilter}
     */
    @Bean(name = "customAuthFilter")
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
