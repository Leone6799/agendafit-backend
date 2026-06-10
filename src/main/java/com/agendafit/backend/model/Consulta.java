package com.agendafit.backend.model;

import com.agendafit.backend.enums.StatusConsulta;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    private StatusConsulta status;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Usuario paciente;

    @ManyToOne
    @JoinColumn(name = "nutricionista_id")
    private Usuario nutricionista;

    @OneToOne
    @JoinColumn(name = "horario_disponivel_id")
    private HorarioDisponivel horarioDisponivel;

    public Consulta() {
    }

    public Consulta(
            LocalDate data,
            LocalTime horario,
            StatusConsulta status,
            Usuario paciente,
            Usuario nutricionista,
            HorarioDisponivel horarioDisponivel
    ) {
        this.data = data;
        this.horario = horario;
        this.status = status;
        this.paciente = paciente;
        this.nutricionista = nutricionista;
        this.horarioDisponivel = horarioDisponivel;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public Usuario getPaciente() {
        return paciente;
    }

    public Usuario getNutricionista() {
        return nutricionista;
    }

    public HorarioDisponivel getHorarioDisponivel() {
        return horarioDisponivel;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    public void setPaciente(Usuario paciente) {
        this.paciente = paciente;
    }

    public void setNutricionista(Usuario nutricionista) {
        this.nutricionista = nutricionista;
    }

    public void setHorarioDisponivel(HorarioDisponivel horarioDisponivel) {
        this.horarioDisponivel = horarioDisponivel;
    }
}