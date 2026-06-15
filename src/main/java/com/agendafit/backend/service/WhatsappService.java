package com.agendafit.backend.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.ResponseEntity;

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
            
            // MUDANÇA 1: O UltraMsg exige o formato URLENCODED na maioria de suas instâncias
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Tratamento do número para o formato internacional (Adicionando o 55)
            String numeroLimpo = telefone.replaceAll("\\D", "");
            if (numeroLimpo.length() == 10 || numeroLimpo.length() == 11) {
                numeroLimpo = "55" + numeroLimpo;
            }

            // MUDANÇA 2: Usando MultiValueMap em vez de HashMap/JSON
            MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
            payload.add("token", apiToken); 
            payload.add("to", numeroLimpo);
            payload.add("body", texto);
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(payload, headers);
            
            // Dispara a requisição e captura a resposta
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            
            System.out.println("Notificação WhatsApp disparada para: " + numeroLimpo);
            // MUDANÇA 3: Isso vai mostrar exatamente o que o servidor do UltraMsg respondeu!
            System.out.println("Resposta do UltraMsg: " + response.getBody()); 

        } catch (Exception e) {
            System.err.println("Erro ao notificar no WhatsApp: " + e.getMessage());
            e.printStackTrace(); 
        }
    }
}