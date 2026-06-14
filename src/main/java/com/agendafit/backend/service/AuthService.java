package com.agendafit.backend.service;

import com.agendafit.backend.dto.CadastroRequest;
import com.agendafit.backend.dto.LoginRequest;
import com.agendafit.backend.enums.TipoUsuario;
import com.agendafit.backend.model.Usuario;
import com.agendafit.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario login(LoginRequest request) {
        // Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválidos"));

        // Compara a senha em texto puro digitada com a criptografada do banco
        if (passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            return usuario;
        } else {
            throw new RuntimeException("E-mail ou senha inválidos");
        }
    }

    // NOME CORRIGIDO PARA BATER COM O CONTROLLER
    public Usuario cadastrarPaciente(CadastroRequest request) {
        // Verifica se o e-mail já existe
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(request.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("E-mail já está em uso");
        }

        // Monta o novo usuário forçando o tipo PACIENTE nativamente
        Usuario novoUsuario = new Usuario(
                request.getNome(),
                request.getIdade(),
                request.getEmail(),
                request.getSenha(), // O Usuario.java intercepta e criptografa
                request.getTelefone(),
                TipoUsuario.PACIENTE // Definido diretamente (resolve o erro do getTipo)
        );

        return usuarioRepository.save(novoUsuario);
    }
}