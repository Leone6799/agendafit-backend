package com.agendafit.backend.service;

import com.agendafit.backend.dto.AgendamentoRequest;
import com.agendafit.backend.model.Consulta;
import com.agendafit.backend.model.HorarioDisponivel;
import com.agendafit.backend.model.Usuario;
import com.agendafit.backend.repository.ConsultaRepository;
import com.agendafit.backend.repository.HorarioDisponivelRepository;
import com.agendafit.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final HorarioDisponivelRepository horarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final WhatsappService whatsappService;

    public ConsultaService(ConsultaRepository consultaRepository, 
                           HorarioDisponivelRepository horarioRepository, 
                           UsuarioRepository usuarioRepository,
                           WhatsappService whatsappService) {
        this.consultaRepository = consultaRepository;
        this.horarioRepository = horarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.whatsappService = whatsappService;
    }

    @Transactional
    public Consulta agendarConsulta(AgendamentoRequest request) {
        System.out.println("Tentando agendar: PacienteID=" + request.getPacienteId() + " HorarioID=" + request.getHorarioId());
        Usuario p = usuarioRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        
        HorarioDisponivel h = horarioRepository.findById(request.getHorarioId())
                .orElseThrow(() -> new RuntimeException("Horário não encontrado"));

        if (h.getDisponivel() == null || !h.getDisponivel()) {
            throw new RuntimeException("Horário indisponível");
        }

        Consulta c = new Consulta();
        c.setPaciente(p);
        
        // CORREÇÃO: Usando o método exato que existe na sua classe Consulta.java
        c.setHorarioDisponivel(h);
        
        // Também setamos a data e hora na consulta se for necessário pela regra de negócio
        c.setData(h.getData());
        c.setHorario(h.getHorario());
        
        h.setDisponivel(false);
        horarioRepository.save(h);
        
        Consulta salvo = consultaRepository.save(c);

        try {
            LocalDate data = h.getData();
            LocalTime hora = h.getHorario();
            String dataHoraStr = (data != null ? data.toString() : "") + " " + (hora != null ? hora.toString() : "");
            whatsappService.notificarAgendamento(p.getTelefone(), p.getNome(), dataHoraStr);
        } catch (Exception e) {
            System.err.println("Erro ao disparar WhatsApp: " + e.getMessage());
        }

        return salvo;
    }

    public List<Consulta> listarConsultasPaciente(Long pacienteId) {
        Usuario p = usuarioRepository.findById(pacienteId).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        return consultaRepository.findByPaciente(p);
    }

    public List<Consulta> listarConsultasAdmin() { return consultaRepository.findAll(); }
    public Consulta buscarPorId(Long id) { return consultaRepository.findById(id).orElse(null); }
    public Consulta confirmarConsulta(Long id) { return consultaRepository.save(buscarPorId(id)); }
    public Consulta cancelarConsulta(Long id) { return consultaRepository.save(buscarPorId(id)); }
    public Consulta concluirConsulta(Long id) { return consultaRepository.save(buscarPorId(id)); }
}