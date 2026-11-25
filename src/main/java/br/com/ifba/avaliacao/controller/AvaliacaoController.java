package br.com.ifba.avaliacao.controller;

import br.com.ifba.avaliacao.dto.AvaliacaoDtos.Atualizar;
import br.com.ifba.avaliacao.dto.AvaliacaoDtos.Criar;
import br.com.ifba.avaliacao.dto.AvaliacaoDtos.VotoRequest;
import br.com.ifba.avaliacao.service.AvaliacaoIService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** Endpoints REST para avaliações e votos. */
@RestController
@RequestMapping(path = "/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController implements AvaliacaoIController {
    private final AvaliacaoIService avaliacaoService;

    /** Lista avaliações, com filtro opcional por item.
     * @return avaliações e HTTP 200
     */
    @Override @GetMapping(path = {"", "/findall"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(@RequestParam(required=false) Long itemId) {
        return ResponseEntity.status(HttpStatus.OK).body(avaliacaoService.listar(itemId));
    }
    /** Busca avaliação por identificador.
     * @return avaliação e HTTP 200
     */
    @Override @GetMapping(path = {"/{id}", "/findbyid/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) { return ResponseEntity.status(HttpStatus.OK).body(avaliacaoService.buscar(id)); }
    /** Publica avaliação para o usuário autenticado.
     * @return avaliação criada e HTTP 201
     */
    @Override @PostMapping(path = {"", "/save"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody Criar dados, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoService.criar(dados, auth.getName()));
    }
    /** Atualiza avaliação do usuário autenticado.
     * @return avaliação atualizada e HTTP 200
     */
    @Override @PutMapping(path = {"/{id}", "/update/{id}"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Atualizar dados, Authentication auth) {
        return ResponseEntity.status(HttpStatus.OK).body(avaliacaoService.atualizar(id, dados, auth.getName()));
    }
    /** Exclui avaliação do usuário autenticado.
     * @return resposta sem conteúdo e HTTP 204
     */
    @Override @DeleteMapping(path = {"/{id}", "/delete/{id}"})
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        avaliacaoService.excluir(id, auth.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /** Registra um voto útil ou não útil.
     * @return resposta sem conteúdo e HTTP 201
     */
    @Override @PostMapping(path = "/{id}/votos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> vote(@PathVariable Long id, @Valid @RequestBody VotoRequest dados, Authentication auth) {
        avaliacaoService.votar(id, dados, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /** Remove o voto do usuário autenticado.
     * @return resposta sem conteúdo e HTTP 204
     */
    @Override @DeleteMapping(path = "/{id}/votos")
    public ResponseEntity<?> removeVote(@PathVariable Long id, Authentication auth) {
        avaliacaoService.removerVoto(id, auth.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
