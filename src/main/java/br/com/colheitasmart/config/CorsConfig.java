
package br.com.colheitasmart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    private final String[] origens;

    public CorsConfig(
            @Value("${app.cors.origins:http://localhost:5173}")
            String[] origens) {

        this.origens = origens;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(
            Arrays.stream(origens)
                .map(String::trim)
                .filter(origem -> !origem.isEmpty())
                .toList()
        );

        config.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        config.setAllowedHeaders(
            List.of("Content-Type", "Accept", "X-CSRF-TOKEN")
        );

        config.setExposedHeaders(
            List.of("Location")
        );

        // Permite cookies de sessao entre React e API.
        config.setAllowCredentials(true);

        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
