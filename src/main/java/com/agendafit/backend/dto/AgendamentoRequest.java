package com.agendafit.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class AgendamentoRequest {
    
    // Aceita tanto "pacienteId" quanto "paciente_id" vindo do Front-end
    @JsonAlias({"pacienteId", "paciente_id"})
    private Long pacienteId;

    @JsonAlias({"horarioId", "horario_id"})
    private Long horarioId;

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public Long getHorarioId() { return horarioId; }
    public void setHorarioId(Long horarioId) { this.horarioId = horarioId; }
}