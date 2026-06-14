package com.agendafit.backend.controller;

import com.agendafit.backend.model.HorarioDisponivel;
import com.agendafit.backend.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@CrossOrigin(origins = "*") 
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping("/datas-disponiveis")
    public List<LocalDate> getDatasDisponiveis() {
        return horarioService.listarDatasDisponiveis();
    }

    @GetMapping("/disponiveis")
    public List<HorarioDisponivel> getHorarios(@RequestParam String data) {
        return horarioService.listarHorariosDisponiveis(data);
    }
}