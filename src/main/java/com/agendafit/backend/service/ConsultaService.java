package com.agendafit.backend.service;

import com.agendafit.backend.dto.AgendamentoRequest;
import com.agendafit.backend.enums.StatusConsulta;
import com.agendafit.backend.model.Consulta;
import com.agendafit.backend.model.HorarioDisponivel;
import com.agendafit.backend.model.Usuario;
import com.agendafit.backend.repository.ConsultaRepository;
import com.agendafit.backend.repository.HorarioDisponivelRepository;
import com.agendafit.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private HorarioDisponivelRepository horarioDisponivelRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private WhatsappService whatsappService;

    // ==========================================
    // AGENDAMENTO E NOTIFICAÇÃO
    // ==========================================
    public Consulta agendarConsulta(AgendamentoRequest request) {
        if (request.getPacienteId() == null || request.getHorarioId() == null) {
            throw new RuntimeException("Erro interno: IDs do agendamento chegaram nulos.");
        }

        Usuario paciente = usuarioRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
                
        HorarioDisponivel horarioDisp = horarioDisponivelRepository.findById(request.getHorarioId())
                .orElseThrow(() -> new RuntimeException("Horário não encontrado"));

        if (horarioDisp.getDisponivel() != null && !horarioDisp.getDisponivel()) {
            throw new RuntimeException("O horário selecionado não está mais disponível.");
        }

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        
        // Define o relacionamento com o nutricionista associado ao horário
        if (horarioDisp.getNutricionista() != null) {
            consulta.setNutricionista(horarioDisp.getNutricionista());
        }

        // CORREÇÃO: O objeto Consulta espera a hora exata (LocalTime) e não a entidade inteira
        consulta.setHorario(horarioDisp.getHorario());
        
        // Se a sua entidade Consulta também armazena a data, descomente a linha abaixo:
        // consulta.setData(horarioDisp.getData());
        
        consulta.setStatus(StatusConsulta.PENDENTE);

        // Bloqueia o horário para que mais ninguém o veja disponível
        horarioDisp.setDisponivel(false);
        horarioDisponivelRepository.save(horarioDisp);

        Consulta consultaSalva = consultaRepository.save(consulta);

        // INTEGRAÇÃO WHATSAPP: Alerta de reserva realizada com sucesso
        try {
            if (paciente.getTelefone() != null && !paciente.getTelefone().trim().isEmpty()) {
                String dataStr = horarioDisp.getData() != null ? horarioDisp.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
                String horaStr = horarioDisp.getHorario() != null ? horarioDisp.getHorario().toString() : "";
                String dataHoraFormatada = dataStr + " às " + horaStr;

                whatsappService.notificarAgendamento(paciente.getTelefone(), paciente.getNome(), dataHoraFormatada);
            }
        } catch (Exception e) {
            System.err.println("Erro no envio do Whatsapp de Agendamento: " + e.getMessage());
        }

        return consultaSalva;
    }

    // ==========================================
    // MÉTODOS DE BUSCA E LISTAGEM
    // ==========================================
    public List<Consulta> listarConsultasPaciente(Long pacienteId) {
        return consultaRepository.findAll().stream()
                .filter(c -> c.getPaciente() != null && c.getPaciente().getId().equals(pacienteId))
                .collect(Collectors.toList());
    }

    public List<Consulta> listarConsultasAdmin() {
        return consultaRepository.findAll();
    }

    public Consulta buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    }

    // ==========================================
    // ATUALIZAÇÕES DE STATUS E CANCELAMENTO
    // ==========================================
    public Consulta confirmarConsulta(Long id) {
        return atualizarStatus(id, StatusConsulta.CONFIRMADA);
    }

    public Consulta cancelarConsulta(Long id) {
        return atualizarStatus(id, StatusConsulta.CANCELADA);
    }

    public Consulta concluirConsulta(Long id) {
        return atualizarStatus(id, StatusConsulta.CONCLUIDA); 
    }

    public Consulta atualizarStatus(Long id, StatusConsulta novoStatus) {
        Consulta consulta = buscarPorId(id);
        StatusConsulta statusAnterior = consulta.getStatus();
        
        consulta.setStatus(novoStatus);
        
        // CORREÇÃO DO COMPILADOR: Extrai a hora mapeada na Consulta, pois ela não contém o objeto HorarioDisponivel
        LocalTime horaConsulta = consulta.getHorario();
        
        // Se a consulta foi cancelada, liberta o horário de volta no calendário
        if (novoStatus == StatusConsulta.CANCELADA && horaConsulta != null) {
            // Como temos apenas o LocalTime salvo, buscamos a entidade original na base de dados para atualizar
            horarioDisponivelRepository.findAll().stream()
                    .filter(hd -> hd.getHorario() != null && hd.getHorario().equals(horaConsulta))
                    .findFirst()
                    .ifPresent(hd -> {
                        hd.setDisponivel(true);
                        horarioDisponivelRepository.save(hd);
                    });
        }
        
        Consulta consultaAtualizada = consultaRepository.save(consulta);

        // INTEGRAÇÃO WHATSAPP: Alerta de cancelamento para o paciente
        if (novoStatus == StatusConsulta.CANCELADA && statusAnterior != StatusConsulta.CANCELADA) {
            try {
                if (consultaAtualizada.getPaciente() != null && consultaAtualizada.getPaciente().getTelefone() != null) {
                    String telefone = consultaAtualizada.getPaciente().getTelefone();
                    String nome = consultaAtualizada.getPaciente().getNome();
                    
                    // Utiliza a hora que já estava salva na Consulta
                    String horaStr = horaConsulta != null ? horaConsulta.toString() : "";
                    
                    // Se a entidade Consulta tiver o campo de Data, pode adicioná-lo aqui.
                    // Por enquanto enviaremos apenas com o horário para evitar novos erros.
                    String dataHoraFormatada = "Horário: " + horaStr; 

                    whatsappService.notificarCancelamento(telefone, nome, dataHoraFormatada);
                }
            } catch (Exception e) {
                System.err.println("Erro no envio do Whatsapp de Cancelamento: " + e.getMessage());
            }
        }

        return consultaAtualizada;
    }
}