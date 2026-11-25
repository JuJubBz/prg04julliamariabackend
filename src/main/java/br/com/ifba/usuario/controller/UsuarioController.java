package br.com.ifba.usuario.controller;

import br.com.ifba.usuario.dto.AuthDtos.Cadastro;
import br.com.ifba.usuario.dto.AuthDtos.Login;
import br.com.ifba.usuario.service.UsuarioIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoints REST de cadastro e autenticação de usuários. */
@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class UsuarioController implements UsuarioIController {
    private final UsuarioIService usuarioService;

    /** Cadastra um usuário.
     * @return dados públicos do usuário e HTTP 201
     */
    @Override @PostMapping(path = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Cadastro dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrar(dados));
    }
    /** Autentica um usuário.
     * @return token Bearer e HTTP 200
     */
    @Override @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody Login dados) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.login(dados));
    }
}
