package com.seuprojeto.bank.infrastructure.web.controller;

import com.seuprojeto.bank.application.service.UsuarioService;
import com.seuprojeto.bank.domain.model.Usuario;
import com.seuprojeto.bank.infrastructure.web.dto.UsuarioRegistroRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Void> cadastrar(
            @RequestBody @Valid UsuarioRegistroRequest request, 
            UriComponentsBuilder uriBuilder) {

        // A delegação Sênior: o Controller apenas passa a bola para o Service
        Usuario novoUsuario = usuarioService.cadastrarUsuarioComConta(
                 request.nome(), request.email(), request.senha()
        );

        // 5. Boa prática REST: Retorna 201 Created com o cabeçalho Location apontando para o recurso criado
        var uri = uriBuilder.path("/api/v1/usuarios/{id}").buildAndExpand(novoUsuario.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}