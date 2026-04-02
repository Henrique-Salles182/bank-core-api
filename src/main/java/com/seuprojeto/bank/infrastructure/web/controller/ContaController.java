package com.seuprojeto.bank.infrastructure.web.controller;

import com.seuprojeto.bank.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaRepository contaRepository;

    // Nota de Arquiteto: Retornar Map direto no Controller é aceitável APENAS para endpoints de debug rápido. 
    // Em produção, criaríamos um ContaResponseDTO.
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarContas() {
        var contas = contaRepository.findAll().stream().map(c -> Map.<String, Object>of(
                "dono", c.getUsuario().getNome(),
                "numero_conta", c.getNumeroConta(),
                "saldo", c.getSaldo()
        )).toList();
        
        return ResponseEntity.ok(contas);
    }
}