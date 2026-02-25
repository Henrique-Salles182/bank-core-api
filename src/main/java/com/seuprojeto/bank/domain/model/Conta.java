package com.seuprojeto.bank.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "contas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Exigência do JPA, mas fechado para uso externo
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento omitido por enquanto para focar no saldo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "numero_conta", unique = true, nullable = false, length = 20)
    private String numeroConta;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal saldo;

    @Column(nullable = false, length = 20)
    private String status;

    @Version // O nosso querido Optimistic Locking para evitar Race Conditions
    private Long version;

    // Construtor de inicialização (Factory method é melhor, mas vamos manter simples por agora)
    public Conta(Usuario usuario, String numeroConta) {
        this.usuario = usuario;
        this.numeroConta = numeroConta;
        this.saldo = BigDecimal.ZERO;
        this.status = "ATIVA";
    }

    // REGRA DE NEGÓCIO NA ENTIDADE (Não no Service!)
    public void debitar(BigDecimal valor) {
        if (!"ATIVA".equals(this.status)) {
            throw new IllegalStateException("Conta bloqueada para transações.");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do débito deve ser maior que zero.");
        }
        if (this.saldo.compareTo(valor) < 0) {
            throw new IllegalStateException("Saldo insuficiente.");
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public void creditar(BigDecimal valor) {
        if (!"ATIVA".equals(this.status)) {
            throw new IllegalStateException("Conta bloqueada para transações.");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do crédito deve ser maior que zero.");
        }
        this.saldo = this.saldo.add(valor);
    }
}