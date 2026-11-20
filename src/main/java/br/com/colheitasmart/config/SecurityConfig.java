
package br.com.colheitasmart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        HttpSessionSecurityContextRepository repositorio =
                new HttpSessionSecurityContextRepository();

        HttpSessionCsrfTokenRepository csrfRepository =
                new HttpSessionCsrfTokenRepository();

        csrfRepository.setHeaderName("X-CSRF-TOKEN");

        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf
                .csrfTokenRepository(csrfRepository)
            )
            .securityContext(context -> context
                .securityContextRepository(repositorio)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(
                    SessionCreationPolicy.IF_REQUIRED
                )
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/cadastro",
                    "/auth/login",
                    "/csrf"
                ).permitAll()

                .requestMatchers(
                    HttpMethod.GET,
                    "/status",
                    "/culturas",
                    "/culturas/**"
                ).permitAll()

                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @RestController
    public static class CsrfController {

        @GetMapping("/csrf")
        public CsrfToken csrf(CsrfToken token) {
            return token;
        }
    }
}
