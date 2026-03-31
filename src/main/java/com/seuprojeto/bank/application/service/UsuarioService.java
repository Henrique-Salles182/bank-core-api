package com.seuprojeto.bank.application.service;

import com.seuprojeto.bank.domain.model.Conta;
import com.seuprojeto.bank.domain.model.Usuario;
import com.seuprojeto.bank.domain.repository.ContaRepository;
import com.seuprojeto.bank.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario cadastrarUsuarioComConta(String nome, String email, String senhaPlana) {
        
        // 1. Regra de unicidade
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email já cadastrado no sistema.");
        }

        // 2. Cria e salva o Usuário
        String senhaHash = passwordEncoder.encode(senhaPlana);
        Usuario novoUsuario = new Usuario(nome, email, senhaHash);
        usuarioRepository.save(novoUsuario);

        // 3. Gera um número de conta aleatório de 8 dígitos simulando um banco
        String numeroConta = UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 8);

        // 4. Cria e salva a Conta vinculada ao Usuário recém-criado
        Conta novaConta = new Conta(novoUsuario, numeroConta);
        contaRepository.save(novaConta);

        return novoUsuario;
    }
}