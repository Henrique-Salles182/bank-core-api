package com.seuprojeto.bank.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.seuprojeto.bank.domain.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Lendo a variável do application.yml
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("bank-core-api") // Quem emitiu o token
                    .withSubject(usuario.getEmail()) // O "dono" do token
                    .withClaim("id", usuario.getId()) // Dados extras (Payload)
                    .withClaim("role", usuario.getRole())
                    .withExpiresAt(gerarDataExpiracao()) // Prazo de validade
                    .sign(algorithm); // Assinatura criptográfica
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("bank-core-api")
                    .build()
                    .verify(tokenJWT) // Verifica se a assinatura é válida e não expirou
                    .getSubject(); // Devolve o email extraído
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant gerarDataExpiracao() {
        // Token expira em 2 horas
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}