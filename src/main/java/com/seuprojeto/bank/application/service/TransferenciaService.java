package com.seuprojeto.bank.application.service;

import com.seuprojeto.bank.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final ContaRepository contaRepository;

    @Transactional
    public void transferir(String contaOrigem, String contaDestino, java.math.BigDecimal valor) {
        // A lógica de orquestração entrará aqui!
    }
}