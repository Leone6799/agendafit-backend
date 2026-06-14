package com.agendafit.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsappService {

    @Value("${whatsapp.api.url}")
    private String apiUrl;

    @Value("${whatsapp.api.token}")
    private String apiToken;

    public void notificarAgendamento(String telefone, String pacienteNome, String dataHora) {
        String mensagem = String.format("Olá, %s! Sua consulta com o nutricionista foi reservada com sucesso para %s. Aguardamos você!", pacienteNome, dataHora);
        enviarMensagem(telefone, mensagem);
    }

    public void notificarCancelamento(String telefone, String pacienteNome, String dataHora) {
        String mensagem = String.format("Olá, %s. Informamos que sua consulta agendada para %s foi cancelada pelo nutricionista. Por favor, acesse o aplicativo para reagendar.", pacienteNome, dataHora);
        enviarMensagem(telefone, mensagem);
    }

    private void enviarMensagem(String telefone, String texto) {
        if (telefone == null || telefone.trim().isEmpty()) return;

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("apikey", apiToken); 

            // Tratamento do número para o formato internacional
            String numeroLimpo = telefone.replaceAll("\\D", "");
            if (numeroLimpo.length() == 10 || numeroLimpo.length() == 11) {
                numeroLimpo = "55" + numeroLimpo;
            }

            // Formato de Payload JSON padrão para APIs não-oficiais (Z-Api, Evolution, etc.)
            String payload = String.format("{\"number\": \"%s\", \"textMessage\": {\"text\": \"%s\"}}", numeroLimpo, texto);
            
            HttpEntity<String> request = new HttpEntity<>(payload, headers);
            restTemplate.postForEntity(apiUrl, request, String.class);
            
            System.out.println("Notificação WhatsApp disparada para: " + numeroLimpo);
        } catch (Exception e) {
            System.err.println("Erro ao notificar no WhatsApp: " + e.getMessage());
        }
    }
}