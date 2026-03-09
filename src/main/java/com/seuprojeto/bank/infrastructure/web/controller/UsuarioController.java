package com.seuprojeto.bank.infrastructure.web.controller;

import com.seuprojeto.bank.domain.model.Usuario;
import com.seuprojeto.bank.domain.repository.UsuarioRepository;
import com.seuprojeto.bank.infrastructure.web.dto.UsuarioRegistroRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    
    // Injetamos o PasswordEncoder para hashear a senha antes de salvar!
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional // Fundamental, pois estamos fazendo um INSERT no banco
    public ResponseEntity<Void> cadastrar(
            @RequestBody @Valid UsuarioRegistroRequest request, 
            UriComponentsBuilder uriBuilder) {

        // 1. Verifica se o email já existe (Regra de negócio básica)
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalStateException("Email já cadastrado no sistema.");
        }

        // 2. Criptografa a senha com BCrypt
        String senhaHasheada = passwordEncoder.encode(request.senha());

        // 3. Cria a entidade usando o construtor rico que fizemos lá atrás
        Usuario novoUsuario = new Usuario(request.nome(), request.email(), senhaHasheada);

        // 4. Salva no banco de dados Oracle
        usuarioRepository.save(novoUsuario);

        // 5. Boa prática REST: Retorna 201 Created com o cabeçalho Location apontando para o recurso criado
        var uri = uriBuilder.path("/api/v1/usuarios/{id}").buildAndExpand(novoUsuario.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}