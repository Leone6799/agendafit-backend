package com.agendafit.backend.controller;

import com.agendafit.backend.dto.CadastroRequest;
import com.agendafit.backend.dto.LoginRequest;
import com.agendafit.backend.model.Usuario;
import com.agendafit.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    public Usuario cadastrar(@Valid @RequestBody CadastroRequest request) {
        return authService.cadastrarPaciente(request);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}