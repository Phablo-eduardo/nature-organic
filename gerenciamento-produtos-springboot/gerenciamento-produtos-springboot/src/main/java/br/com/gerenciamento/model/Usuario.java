package br.com.gerenciamento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Informe um e-mail válido.")
    @NotBlank(message = "Informe o e-mail.")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Informe o usuário.")
    @Size(min = 3, max = 20, message = "O usuário precisa conter entre 3 e 20 caracteres.")
    @Column(name = "nome_usuario", nullable = false, unique = true, length = 20)
    private String user;

    @NotBlank(message = "Informe a senha.")
    @Size(min = 4, message = "A senha deve ter pelo menos 4 caracteres.")
    @Column(nullable = false)
    private String senha;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
