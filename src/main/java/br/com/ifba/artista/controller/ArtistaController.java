package br.com.ifba.artista.controller;

import br.com.ifba.artista.entity.Artista;
import br.com.ifba.artista.service.ArtistaIService;
import br.com.ifba.catalogo.dto.ItemDtos.Resposta;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Endpoints REST para artistas. */
@RestController
@RequestMapping(path = {"/artistas", "/api/artistas"})
@RequiredArgsConstructor
public class ArtistaController implements ArtistaIController {
    private final ArtistaIService artistaService;

    /** Lista ou filtra artistas.
     * @return artistas e HTTP 200
     */
    @Override @GetMapping(path = {"", "/findall"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(@RequestParam(required=false) String q) {
        List<Resposta> resposta=artistaService.findAll(q).stream().map(Resposta::de).toList();
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
    /** Cadastra um artista.
     * @return artista criado e HTTP 201
     */
    @Override @PostMapping(path = {"", "/save"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Artista artista) { return ResponseEntity.status(HttpStatus.CREATED).body(Resposta.de(artistaService.save(artista))); }
    /** Atualiza artista.
     * @return artista atualizado e HTTP 200
     */
    @Override @PutMapping(path = {"/update/{id}", "/{id}"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Artista artista) { return ResponseEntity.status(HttpStatus.OK).body(Resposta.de(artistaService.update(id, artista))); }
    /** Exclui artista.
     * @return resposta sem conteúdo e HTTP 204
     */
    @Override @DeleteMapping(path = {"/delete/{id}", "/{id}"})
    public ResponseEntity<?> delete(@PathVariable Long id) { artistaService.delete(id); return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); }
    /** Busca artista por identificador.
     * @return artista encontrado e HTTP 200
     */
    @Override @GetMapping(path = {"/findbyid/{id}", "/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) { return ResponseEntity.status(HttpStatus.OK).body(Resposta.de(artistaService.findById(id))); }
    /** Pesquisa artista pelo nome.
     * @return artistas correspondentes e HTTP 200
     */
    @Override @GetMapping(path = "/findbyname/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByNome(@PathVariable String nome) {
        List<Resposta> resposta=artistaService.findByNome(nome).stream().map(Resposta::de).toList();
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}
