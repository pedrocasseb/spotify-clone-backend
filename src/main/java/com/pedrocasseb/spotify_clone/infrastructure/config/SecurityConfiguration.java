package com.pedrocasseb.spotify_clone.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/logout", "/api/songs") // ✅ Ignora CSRF para upload de arquivos
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/songs").authenticated()
                        .requestMatchers("/api/logout").permitAll()
                        .requestMatchers("/api/get-authenticated-user").authenticated() // ✅ Endpoint do usuário
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("http://localhost:4200", true) // ✅ Redireciona para Angular após login
                        .failureUrl("http://localhost:4200/login?error=true") // ✅ Redireciona em caso de erro
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // ✅ Configura CORS

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ Permite origem do Angular
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        // ✅ Permite todos os métodos HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ Permite todos os headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // ✅ IMPORTANTE: Permite cookies/credentials
        configuration.setAllowCredentials(true);

        // ✅ Expõe headers necessários
        configuration.setExposedHeaders(Arrays.asList("X-XSRF-TOKEN"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}