package com.agendafit.backend.repository;

import com.agendafit.backend.enums.TipoUsuario;
import com.agendafit.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Método que o AuthService usa para procurar quem está tentando logar
    Optional<Usuario> findByEmail(String email);
    
    // Método que seu sistema usa para listar pacientes/nutricionistas
    List<Usuario> findByTipo(TipoUsuario tipo);
}