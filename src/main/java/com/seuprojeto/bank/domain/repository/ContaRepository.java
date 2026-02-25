package com.seuprojeto.bank.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import com.seuprojeto.bank.domain.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    // Método obrigatório para transações financeiras!
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Conta c WHERE c.numeroConta = :numeroConta")
    Optional<Conta> findByNumeroContaWithLock(@Param("numeroConta") String numeroConta);
}