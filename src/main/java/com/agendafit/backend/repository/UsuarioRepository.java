package com.agendafit.backend.repository;

import com.agendafit.backend.enums.TipoUsuario;
import com.agendafit.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByTipo(TipoUsuario tipo);
}