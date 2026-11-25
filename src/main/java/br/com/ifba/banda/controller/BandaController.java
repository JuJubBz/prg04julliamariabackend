package br.com.ifba.banda.controller;

import br.com.ifba.banda.entity.Banda;
import br.com.ifba.banda.service.BandaIService;
import br.com.ifba.catalogo.dto.ItemDtos.Resposta;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Endpoints REST para bandas. */
@RestController
@RequestMapping(path = {"/bandas", "/api/bandas"})
@RequiredArgsConstructor
public class BandaController implements BandaIController {
    private final BandaIService bandaService;

    /** Lista ou filtra bandas.
     * @return bandas e HTTP 200
     */
    @Override @GetMapping(path = {"", "/findall"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(@RequestParam(required=false) String q) {
        List<Resposta> resposta=bandaService.findAll(q).stream().map(Resposta::de).toList();
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
    /** Cadastra uma banda.
     * @return banda criada e HTTP 201
     */
    @Override @PostMapping(path = {"", "/save"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Banda banda) { return ResponseEntity.status(HttpStatus.CREATED).body(Resposta.de(bandaService.save(banda))); }
    /** Atualiza uma banda.
     * @return banda atualizada e HTTP 200
     */
    @Override @PutMapping(path = {"/update/{id}", "/{id}"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Banda banda) { return ResponseEntity.status(HttpStatus.OK).body(Resposta.de(bandaService.update(id, banda))); }
    /** Exclui uma banda.
     * @return resposta sem conteúdo e HTTP 204
     */
    @Override @DeleteMapping(path = {"/delete/{id}", "/{id}"})
    public ResponseEntity<?> delete(@PathVariable Long id) { bandaService.delete(id); return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); }
    /** Busca banda por identificador.
     * @return banda encontrada e HTTP 200
     */
    @Override @GetMapping(path = {"/findbyid/{id}", "/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) { return ResponseEntity.status(HttpStatus.OK).body(Resposta.de(bandaService.findById(id))); }
    /** Pesquisa bandas pelo nome.
     * @return bandas correspondentes e HTTP 200
     */
    @Override @GetMapping(path = "/findbyname/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByNome(@PathVariable String nome) {
        List<Resposta> resposta=bandaService.findByNome(nome).stream().map(Resposta::de).toList();
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}
