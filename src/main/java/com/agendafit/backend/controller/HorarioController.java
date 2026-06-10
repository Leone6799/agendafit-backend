package com.agendafit.backend.controller;

import com.agendafit.backend.model.HorarioDisponivel;
import com.agendafit.backend.service.HorarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/horarios")
public class HorarioController {

    private final HorarioService horarioService;

    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @GetMapping("/datas-disponiveis")
    public List<LocalDate> listarDatasDisponiveis() {
        return horarioService.listarDatasDisponiveis();
    }

    @GetMapping("/disponiveis")
    public List<HorarioDisponivel> listarHorariosDisponiveis(@RequestParam String data) {
        return horarioService.listarHorariosDisponiveis(data);
    }
}
