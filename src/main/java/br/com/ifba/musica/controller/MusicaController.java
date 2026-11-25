package br.com.ifba.musica.controller;

import br.com.ifba.catalogo.dto.ItemDtos.Resposta;
import br.com.ifba.musica.entity.Musica;
import br.com.ifba.musica.service.MusicaIService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Endpoints REST para músicas. */
@RestController
@RequestMapping(path = {"/musicas", "/api/musicas"})
@RequiredArgsConstructor
public class MusicaController implements MusicaIController {
    private final MusicaIService musicaService;

    /** Lista ou filtra músicas.
     * @return músicas e HTTP 200
     */
    @Override @GetMapping(path = {"", "/findall"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(@RequestParam(required=false) String q) {
        List<Resposta> resposta=musicaService.findAll(q).stream().map(Resposta::de).toList();
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
    /** Cadastra uma música.
     * @return música criada e HTTP 201
     */
    @Override @PostMapping(path = {"", "/save"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Musica musica) { return ResponseEntity.status(HttpStatus.CREATED).body(Resposta.de(musicaService.save(musica))); }
    /** Atualiza uma música.
     * @return música atualizada e HTTP 200
     */
    @Override @PutMapping(path = {"/update/{id}", "/{id}"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Musica musica) { return ResponseEntity.status(HttpStatus.OK).body(Resposta.de(musicaService.update(id, musica))); }
    /** Exclui uma música.
     * @return resposta sem conteúdo e HTTP 204
     */
    @Override @DeleteMapping(path = {"/delete/{id}", "/{id}"})
    public ResponseEntity<?> delete(@PathVariable Long id) { musicaService.delete(id); return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); }
    /** Busca música por identificador.
     * @return música encontrada e HTTP 200
     */
    @Override @GetMapping(path = {"/findbyid/{id}", "/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) { return ResponseEntity.status(HttpStatus.OK).body(Resposta.de(musicaService.findById(id))); }
    /** Pesquisa músicas pelo nome.
     * @return músicas correspondentes e HTTP 200
     */
    @Override @GetMapping(path = "/findbyname/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByNome(@PathVariable String nome) {
        List<Resposta> resposta=musicaService.findByNome(nome).stream().map(Resposta::de).toList();
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}
