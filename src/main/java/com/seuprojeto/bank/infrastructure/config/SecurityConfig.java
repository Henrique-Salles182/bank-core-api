package com.seuprojeto.bank.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Desligando o CSRF (pois a API é Stateless)
                .csrf(csrf -> csrf.disable())
                
                // 2. Definindo a política de sessão como Stateless (NÃO usar cookies de sessão)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // 3. Configurando o controle de rotas
                .authorizeHttpRequests(req -> {
                    // Endpoint de login e cadastro de usuários devem ser públicos
                    req.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll();
                    
                    // Qualquer outra requisição (como a nossa de transferências) EXIGE autenticação
                    req.anyRequest().authenticated();
                })
                .build();
    }

    // Bean responsável por criptografar senhas (exigência do escopo inicial)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}