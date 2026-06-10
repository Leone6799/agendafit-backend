package com.agendafit.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "horarios_disponiveis")
public class HorarioDisponivel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private LocalTime horario;

    private Boolean disponivel;

    @ManyToOne
    @JoinColumn(name = "nutricionista_id")
    private Usuario nutricionista;

    public HorarioDisponivel() {
    }

    public HorarioDisponivel(LocalDate data, LocalTime horario, Boolean disponivel, Usuario nutricionista) {
        this.data = data;
        this.horario = horario;
        this.disponivel = disponivel;
        this.nutricionista = nutricionista;
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

    public Boolean getDisponivel() {
        return disponivel;
    }

    public Usuario getNutricionista() {
        return nutricionista;
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

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public void setNutricionista(Usuario nutricionista) {
        this.nutricionista = nutricionista;
    }
}