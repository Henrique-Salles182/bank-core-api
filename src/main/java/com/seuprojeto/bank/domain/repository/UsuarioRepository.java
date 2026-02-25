package com.seuprojeto.bank.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seuprojeto.bank.domain.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    
}