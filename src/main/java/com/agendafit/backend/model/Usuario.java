package com.agendafit.backend.model;

import com.agendafit.backend.enums.TipoUsuario;
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer idade;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(name = "telefone")
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipo; 

    public Usuario() {
    }

    public Usuario(String nome, Integer idade, String email, String senha, String telefone, TipoUsuario tipo) {
        this.nome = nome;
        this.idade = idade;
        this.email = email;
        this.setSenha(senha); // Redireciona para o nosso setter inteligente abaixo
        this.telefone = telefone;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public TipoUsuario getTipo() { return tipo; } 
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; } 

    public String getSenha() { return senha; }

    // ==========================================
    // SISTEMA DE SEGURANÇA E CRIPTOGRAFIA
    // ==========================================
    public void setSenha(String senha) {
        // Se a senha não for nula e NÃO começar com "$2a$" (que é a assinatura do BCrypt), ele criptografa.
        if (senha != null && !senha.startsWith("$2a$")) {
            this.senha = new BCryptPasswordEncoder().encode(senha);
        } else {
            // Se já estiver criptografada, apenas salva.
            this.senha = senha;
        }
    }
}