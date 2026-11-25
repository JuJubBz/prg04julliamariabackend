package br.com.ifba.usuario.service;

import br.com.ifba.usuario.dto.AuthDtos.Cadastro;
import br.com.ifba.usuario.dto.AuthDtos.Login;
import br.com.ifba.usuario.dto.AuthDtos.Token;
import br.com.ifba.usuario.dto.AuthDtos.UsuarioResposta;

/** Contrato de cadastro e autenticação de usuários. */
public interface UsuarioIService {
    UsuarioResposta cadastrar(Cadastro dados);
    Token login(Login dados);
}
