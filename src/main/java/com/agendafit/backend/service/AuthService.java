package com.agendafit.backend.service;

import com.agendafit.backend.dto.CadastroRequest;
import com.agendafit.backend.dto.LoginRequest;
import com.agendafit.backend.enums.TipoUsuario;
import com.agendafit.backend.model.Usuario;
import com.agendafit.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(
            UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario cadastrarPaciente(CadastroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Já existe usuário cadastrado com esse e-mail.");
        }

        String senhaCriptografada = passwordEncoder.encode(request.getSenha());

        Usuario usuario = new Usuario(
                request.getNome(),
                request.getIdade(),
                request.getEmail(),
                request.getTelefone(),
                senhaCriptografada,
                TipoUsuario.PACIENTE
        );

        return usuarioRepository.save(usuario);
    }

    public Usuario login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválidos."));

        boolean senhaCorreta = passwordEncoder.matches(
                request.getSenha(),
                usuario.getSenha()
        );

        if (!senhaCorreta) {
            throw new RuntimeException("E-mail ou senha inválidos.");
        }

        return usuario;
    }
}