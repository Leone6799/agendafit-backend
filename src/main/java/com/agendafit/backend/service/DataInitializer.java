package com.agendafit.backend.service;

import com.agendafit.backend.enums.TipoUsuario;
import com.agendafit.backend.model.HorarioDisponivel;
import com.agendafit.backend.model.Usuario;
import com.agendafit.backend.repository.HorarioDisponivelRepository;
import com.agendafit.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HorarioDisponivelRepository horarioRepository;

    @Override
    public void run(String... args) throws Exception {
        Usuario admin;
        if (usuarioRepository.count() == 0) {
            admin = new Usuario("Nutricionista Admin", 30, "admin@nutri.com", "123456", "5511999999999", TipoUsuario.NUTRICIONISTA);
            admin = usuarioRepository.save(admin);
        } else {
            admin = usuarioRepository.findAll().get(0);
        }

        if (horarioRepository.count() == 0) {
            LocalDate hoje = LocalDate.now();
            for (int i = 0; i < 7; i++) {
                LocalDate dataTeste = hoje.plusDays(i);
                // Agora passamos o 'admin' como 4º argumento, satisfazendo o compilador
                horarioRepository.save(new HorarioDisponivel(dataTeste, LocalTime.of(9, 0), true, admin));
                horarioRepository.save(new HorarioDisponivel(dataTeste, LocalTime.of(14, 0), true, admin));
                horarioRepository.save(new HorarioDisponivel(dataTeste, LocalTime.of(16, 0), true, admin));
            }
        }
    }
}