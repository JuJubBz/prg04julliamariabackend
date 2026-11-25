package br.com.ifba.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDtos {
    private AuthDtos() {}
    public record Cadastro(@NotBlank @Size(max=100) String nome, @NotBlank @Email @Size(max=180) String email, @NotBlank @Size(min=8,max=72) String senha) {}
    public record Login(@NotBlank @Email String email, @NotBlank String senha) {}
    public record Token(String token, String tipo) {}
    public record UsuarioResposta(Long id, String nome, String email) {}
}
