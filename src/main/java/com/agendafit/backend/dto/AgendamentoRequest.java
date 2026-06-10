package com.agendafit.backend.dto;

public class AgendamentoRequest {

    private Long pacienteId;
    private Long horarioId;

    public Long getPacienteId() {
        return pacienteId;
    }

    public Long getHorarioId() {
        return horarioId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public void setHorarioId(Long horarioId) {
        this.horarioId = horarioId;
    }
}