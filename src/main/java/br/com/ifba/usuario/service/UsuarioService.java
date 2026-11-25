package br.com.ifba.usuario.service;

import br.com.ifba.infrastructure.config.JwtService;
import br.com.ifba.usuario.dto.AuthDtos.*;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioIService {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    @Override public UsuarioResposta cadastrar(Cadastro dados) {
        String email=dados.email().trim().toLowerCase();
        if (usuarios.existsByEmailIgnoreCase(email)) throw new ResponseStatusException(HttpStatus.CONFLICT,"E-mail já cadastrado");
        Usuario u=usuarios.save(new Usuario(dados.nome().trim(),email,encoder.encode(dados.senha())));
        return new UsuarioResposta(u.getId(),u.getNome(),u.getEmail());
    }
    @Override public Token login(Login dados) {
        Usuario u=usuarios.findByEmailIgnoreCase(dados.email().trim()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciais inválidas"));
        if (!encoder.matches(dados.senha(),u.getSenhaHash())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciais inválidas");
        return new Token(jwt.issue(u.getEmail()),"Bearer");
    }
}
