package com.seuprojeto.bank.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seuprojeto.bank.domain.model.Usuario;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Método obrigatório para o Spring Security buscar o usuário na hora do login
    Optional<Usuario> findByEmail(String email);
}