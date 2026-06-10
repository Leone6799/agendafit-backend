package com.agendafit.backend.service;

import com.agendafit.backend.dto.AgendamentoRequest;
import com.agendafit.backend.enums.StatusConsulta;
import com.agendafit.backend.enums.TipoUsuario;
import com.agendafit.backend.model.Consulta;
import com.agendafit.backend.model.HorarioDisponivel;
import com.agendafit.backend.model.Usuario;
import com.agendafit.backend.repository.ConsultaRepository;
import com.agendafit.backend.repository.HorarioDisponivelRepository;
import com.agendafit.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final UsuarioRepository usuarioRepository;
    private final HorarioDisponivelRepository horarioDisponivelRepository;

    public ConsultaService(
            ConsultaRepository consultaRepository,
            UsuarioRepository usuarioRepository,
            HorarioDisponivelRepository horarioDisponivelRepository
    ) {
        this.consultaRepository = consultaRepository;
        this.usuarioRepository = usuarioRepository;
        this.horarioDisponivelRepository = horarioDisponivelRepository;
    }

    public Consulta agendarConsulta(AgendamentoRequest request) {
        Usuario paciente = usuarioRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

        if (paciente.getTipo() != TipoUsuario.PACIENTE) {
            throw new RuntimeException("Apenas pacientes podem agendar consultas.");
        }

        HorarioDisponivel horarioDisponivel = horarioDisponivelRepository.findById(request.getHorarioId())
                .orElseThrow(() -> new RuntimeException("Horário não encontrado."));

        if (horarioDisponivel.getData().isBefore(LocalDate.now())) {
            throw new RuntimeException("Não é possível agendar uma consulta em data passada.");
        }

        if (!horarioDisponivel.getDisponivel()) {
            throw new RuntimeException("Esse horário não está disponível.");
        }

        horarioDisponivel.setDisponivel(false);
        horarioDisponivelRepository.save(horarioDisponivel);

        Consulta consulta = new Consulta(
                horarioDisponivel.getData(),
                horarioDisponivel.getHorario(),
                StatusConsulta.PENDENTE,
                paciente,
                horarioDisponivel.getNutricionista(),
                horarioDisponivel
        );

        return consultaRepository.save(consulta);
    }

    public List<Consulta> listarConsultasPaciente(Long pacienteId) {
        Usuario paciente = usuarioRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

        return consultaRepository.findByPaciente(paciente);
    }

    public List<Consulta> listarConsultasAdmin() {
        return consultaRepository.findAll();
    }

    public Consulta buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada."));
    }

    public Consulta confirmarConsulta(Long id) {
        Consulta consulta = buscarPorId(id);
        consulta.setStatus(StatusConsulta.CONFIRMADA);
        return consultaRepository.save(consulta);
    }

    public Consulta cancelarConsulta(Long id) {
        Consulta consulta = buscarPorId(id);
        consulta.setStatus(StatusConsulta.CANCELADA);

        HorarioDisponivel horario = consulta.getHorarioDisponivel();
        horario.setDisponivel(true);
        horarioDisponivelRepository.save(horario);

        return consultaRepository.save(consulta);
    }

    public Consulta concluirConsulta(Long id) {
        Consulta consulta = buscarPorId(id);
        consulta.setStatus(StatusConsulta.CONCLUIDA);
        return consultaRepository.save(consulta);
    }
}