package com.seuprojeto.bank.infrastructure.web.controller;

import com.seuprojeto.bank.domain.model.Usuario;
import com.seuprojeto.bank.infrastructure.security.TokenService;
import com.seuprojeto.bank.infrastructure.web.dto.AutenticacaoRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> efetuarLogin(@RequestBody @Valid AutenticacaoRequest request) {
        
        // 1. Cria o token "cru" (não autenticado ainda) com os dados que o usuário enviou
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        
        // 2. O Spring Security pega esse token, vai no banco, criptografa a senha enviada, e compara.
        // Se a senha estiver errada, ele lança exceção (BadCredentialsException) e nem passa daqui.
        var authentication = manager.authenticate(authenticationToken);
        
        // 3. Se chegou aqui, a senha está correta! Pegamos o usuário autenticado.
        var usuario = (Usuario) authentication.getPrincipal();
        
        // 4. Geramos o nosso JWT (A Pulseirinha)
        var tokenJWT = tokenService.gerarToken(usuario);
        
        // 5. Devolvemos para o cliente
        return ResponseEntity.ok(Map.of("token", tokenJWT));
    }
}