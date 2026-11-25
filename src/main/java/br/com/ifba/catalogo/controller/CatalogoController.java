package br.com.ifba.catalogo.controller;

import br.com.ifba.catalogo.dto.ItemDtos.Criar;
import br.com.ifba.catalogo.service.CatalogoIService;
import br.com.ifba.infrastructure.entity.TipoItem;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** Endpoints REST agregados para o catálogo musical. */
@RestController
@RequestMapping(path = {"/api/catalogos", "/api/itens"})
@RequiredArgsConstructor
public class CatalogoController implements CatalogoIController {
    private final CatalogoIService catalogoService;

    /** Pesquisa catálogo, com filtros opcionais.
     * @return itens musicais e HTTP 200
     */
    @Override @GetMapping(path = {"", "/findall"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(@RequestParam(required=false) TipoItem tipo, @RequestParam(required=false) String q) {
        return ResponseEntity.status(HttpStatus.OK).body(catalogoService.pesquisar(tipo, q));
    }
    /** Cria item do tipo informado.
     * @return item criado e HTTP 201
     */
    @Override @PostMapping(path = {"", "/save"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody Criar dados, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.criar(dados));
    }
}
