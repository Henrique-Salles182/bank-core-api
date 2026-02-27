package com.seuprojeto.bank.infrastructure.web.controller;

import com.seuprojeto.bank.application.service.TransferenciaService;
import com.seuprojeto.bank.infrastructure.web.dto.TransferenciaRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transferencias")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<Void> transferir(
            @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey,
            @RequestBody @Valid TransferenciaRequest request) {

        // Nota de Arquitetura: Em um cenário real, um Interceptor ou Filtro interceptaria 
        // a Idempotency-Key antes de chegar aqui, verificaria no Redis/Banco e barraria a duplicata.
        // Para este passo, estamos apenas exigindo a chave no cabeçalho HTTP.

        transferenciaService.transferir(request.contaOrigem(), request.contaDestino(), request.valor());

        return ResponseEntity.ok().build(); 
    }
}