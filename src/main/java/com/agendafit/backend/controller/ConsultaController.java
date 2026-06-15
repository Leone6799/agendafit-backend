package com.agendafit.backend.controller;

import com.agendafit.backend.dto.AgendamentoRequest;
import com.agendafit.backend.model.Consulta;
import com.agendafit.backend.service.ConsultaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/consultas")
@CrossOrigin(origins = "*")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping("/agendar")
    public Consulta agendar(@RequestBody AgendamentoRequest request) {
        return consultaService.agendarConsulta(request);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<Consulta> listarConsultasPaciente(@PathVariable Long pacienteId) {
        return consultaService.listarConsultasPaciente(pacienteId);
    }

    @GetMapping 
    public List<Consulta> listarConsultasAdmin() {
        return consultaService.listarConsultasAdmin();
    }

    @GetMapping("/{id}")
    public Consulta buscarPorId(@PathVariable Long id) {
        return consultaService.buscarPorId(id);
    }

    @PutMapping("/{id}/confirmar")
    public Consulta confirmar(@PathVariable Long id) {
        return consultaService.confirmarConsulta(id);
    }

    @PutMapping("/{id}/cancelar")
    public Consulta cancelar(@PathVariable Long id) {
        return consultaService.cancelarConsulta(id);
    }

    @PutMapping("/{id}/concluir")
    public Consulta concluir(@PathVariable Long id) {
        return consultaService.concluirConsulta(id);
    }
}