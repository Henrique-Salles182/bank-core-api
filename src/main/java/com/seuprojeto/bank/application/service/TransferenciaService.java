package com.seuprojeto.bank.application.service;

import com.seuprojeto.bank.domain.repository.ContaRepository;
import com.seuprojeto.bank.domain.model.Conta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final ContaRepository contaRepository;

    @Transactional
    public void transferir(String contaOrigem, String contaDestino, BigDecimal valor) {
        
        // 1. Validação Básica
        if (contaOrigem.equals(contaDestino)) {
            throw new IllegalArgumentException("Contas de origem e destino não podem ser iguais.");
        }

        // 2. Prevenção de Deadlock: Ordenação Lexicográfica das Contas
        String primeiraContaLock = contaOrigem.compareTo(contaDestino) < 0 ? contaOrigem : contaDestino;
        String segundaContaLock = contaOrigem.compareTo(contaDestino) < 0 ? contaDestino : contaOrigem;

        // 3. Obtendo as contas com Pessimistic Lock na ordem correta
        Conta conta1 = contaRepository.findByNumeroContaWithLock(primeiraContaLock)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada: " + primeiraContaLock));
        
        Conta conta2 = contaRepository.findByNumeroContaWithLock(segundaContaLock)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada: " + segundaContaLock));

        // 4. Identificando quem é quem após os locks
        Conta origem = conta1.getNumeroConta().equals(contaOrigem) ? conta1 : conta2;
        Conta destino = conta1.getNumeroConta().equals(contaDestino) ? conta1 : conta2;

        // 5. Execução do Domínio (Regras de Negócio blindadas nas Entidades)
        origem.debitar(valor);
        destino.creditar(valor);

        // O Hibernate detecta as mudanças (Dirty Checking) e fará o UPDATE automaticamente 
        // no fim da transação. Não precisamos chamar contaRepository.save().
    }
}