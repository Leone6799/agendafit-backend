package com.agendafit.backend.service;

import com.agendafit.backend.enums.TipoUsuario;
import com.agendafit.backend.model.HorarioDisponivel;
import com.agendafit.backend.model.Usuario;
import com.agendafit.backend.repository.HorarioDisponivelRepository;
import com.agendafit.backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final HorarioDisponivelRepository horarioDisponivelRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            HorarioDisponivelRepository horarioDisponivelRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.horarioDisponivelRepository = horarioDisponivelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Usuario nutricionista = usuarioRepository.findByEmail("nutri@agendafit.com")
                .orElseGet(() -> {
                    Usuario novoNutricionista = new Usuario(
                            "Nutricionista Admin",
                            32,
                            "nutri@agendafit.com",
                            "71999999999",
                            passwordEncoder.encode("123456"),
                            TipoUsuario.NUTRICIONISTA
                    );

                    return usuarioRepository.save(novoNutricionista);
                });

        LocalTime[] horarios = {
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalTime.of(14, 0),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0)
        };

        for (int dia = 1; dia <= 20; dia++) {
            LocalDate data = LocalDate.now().plusDays(dia);

            for (LocalTime horario : horarios) {
                boolean jaExiste = horarioDisponivelRepository
                        .existsByDataAndHorarioAndNutricionista(data, horario, nutricionista);

                if (!jaExiste) {
                    HorarioDisponivel horarioDisponivel = new HorarioDisponivel(
                            data,
                            horario,
                            true,
                            nutricionista
                    );

                    horarioDisponivelRepository.save(horarioDisponivel);
                }
            }
        }
    }
}