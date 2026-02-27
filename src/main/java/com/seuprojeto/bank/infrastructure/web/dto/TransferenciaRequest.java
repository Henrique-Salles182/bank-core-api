package com.seuprojeto.bank.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TransferenciaRequest(
        @NotBlank(message = "A conta de origem e obrigatória") 
        String contaOrigem,

        @NotBlank(message = "A conta de destino é obrigatória")
        String contaDestino,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor da transferência deve ser maior que zero")
        BigDecimal valor
){}
