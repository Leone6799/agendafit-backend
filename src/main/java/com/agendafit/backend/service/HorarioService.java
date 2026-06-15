package com.agendafit.backend.service;

import com.agendafit.backend.model.HorarioDisponivel;
import com.agendafit.backend.repository.HorarioDisponivelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class HorarioService {

    @Autowired
    private HorarioDisponivelRepository repository;

    public List<LocalDate> listarDatasDisponiveis() {
        // Usa a query do banco de dados que já filtra por "disponível = true",
        // remove duplicatas e traz apenas datas a partir de hoje.
        return repository.buscarDatasDisponiveis(LocalDate.now());
    }

    public List<HorarioDisponivel> listarHorariosDisponiveis(String data) {
        LocalDate dataConvertida = LocalDate.parse(data);
        
        // Usa o método inteligente do Spring Data que busca apenas horários daquela data,
        // garantindo que "disponível = true" e já organizando as horas de forma crescente.
        return repository.findByDataAndDisponivelTrueOrderByHorarioAsc(dataConvertida);
    }
}