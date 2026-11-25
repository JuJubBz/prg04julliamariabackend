package br.com.ifba.usuario.controller;

import br.com.ifba.usuario.dto.AuthDtos.Cadastro;
import br.com.ifba.usuario.dto.AuthDtos.Login;
import org.springframework.http.ResponseEntity;

/** Contrato REST para cadastro e autenticação de usuários. */
public interface UsuarioIController {
    ResponseEntity<?> cadastrar(Cadastro dados);
    ResponseEntity<?> login(Login dados);
}
