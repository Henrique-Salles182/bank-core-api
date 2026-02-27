package com.seuprojeto.bank.infrastructure.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura nossas regras de negócio (ex: Saldo Insuficiente, Conta Bloqueada)
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleBusinessRuleViolation(IllegalStateException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setTitle("Violação de Regra de Negócio");
        problemDetail.setType(URI.create("https://api.banco.com/erros/regra-de-negocio"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    // Captura erros de validação de DTO (ex: Conta origem vazia, valor negativo)
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Requisição Inválida");
        return problemDetail;
    }

    // Omitido aqui por brevidade, mas você deve criar um para MethodArgumentNotValidException
    // para extrair as mensagens do @Valid do seu DTO e colocá-las no ProblemDetail.
}