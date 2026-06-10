package com.agendafit.backend.repository;

import com.agendafit.backend.model.HorarioDisponivel;
import com.agendafit.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Long> {

    @Query("SELECT DISTINCT h.data FROM HorarioDisponivel h WHERE h.disponivel = true AND h.data >= :hoje ORDER BY h.data")
    List<LocalDate> buscarDatasDisponiveis(LocalDate hoje);

    List<HorarioDisponivel> findByDataAndDisponivelTrueOrderByHorarioAsc(LocalDate data);

    boolean existsByDataAndHorarioAndNutricionista(
            LocalDate data,
            LocalTime horario,
            Usuario nutricionista
    );
}