package com.agendafit.backend.service;

import com.agendafit.backend.model.HorarioDisponivel;
import com.agendafit.backend.repository.HorarioDisponivelRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class HorarioService {

    private final HorarioDisponivelRepository horarioDisponivelRepository;

    public HorarioService(HorarioDisponivelRepository horarioDisponivelRepository) {
        this.horarioDisponivelRepository = horarioDisponivelRepository;
    }

    public List<LocalDate> listarDatasDisponiveis() {
        return horarioDisponivelRepository.buscarDatasDisponiveis(LocalDate.now());
    }

    public List<HorarioDisponivel> listarHorariosDisponiveis(String data) {
        LocalDate dataConvertida = LocalDate.parse(data);

        if (dataConvertida.isBefore(LocalDate.now())) {
            return Collections.emptyList();
        }

        return horarioDisponivelRepository.findByDataAndDisponivelTrueOrderByHorarioAsc(dataConvertida);
    }
}
