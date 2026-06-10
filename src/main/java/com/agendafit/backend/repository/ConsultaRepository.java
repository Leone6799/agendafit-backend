package com.agendafit.backend.repository;

import com.agendafit.backend.model.Consulta;
import com.agendafit.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByPaciente(Usuario paciente);

    List<Consulta> findByNutricionista(Usuario nutricionista);
}