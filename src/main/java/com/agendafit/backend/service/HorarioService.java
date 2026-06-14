package com.agendafit.backend.service;

import com.agendafit.backend.model.HorarioDisponivel;
import com.agendafit.backend.repository.HorarioDisponivelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorarioService {

    @Autowired
    private HorarioDisponivelRepository repository;

    public List<LocalDate> listarDatasDisponiveis() {
        return repository.findAll().stream()
                .filter(HorarioDisponivel::getDisponivel)
                .map(HorarioDisponivel::getData)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<HorarioDisponivel> listarHorariosDisponiveis(String data) {
        LocalDate dataConvertida = LocalDate.parse(data);
        return repository.findAll().stream()
                .filter(h -> h.getData().equals(dataConvertida) && h.getDisponivel())
                .collect(Collectors.toList());
    }
}