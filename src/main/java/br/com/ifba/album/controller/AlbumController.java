package br.com.ifba.album.controller;

import br.com.ifba.album.entity.Album;
import br.com.ifba.album.service.AlbumIService;
import br.com.ifba.catalogo.dto.ItemDtos.Resposta;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoints REST para consultar e administrar álbuns. */
@RestController
@RequestMapping(path = {"/albums", "/api/albuns"})
@RequiredArgsConstructor
public class AlbumController implements AlbumIController {
    private final AlbumIService albumService;

    /** Lista todos os álbuns cadastrados.
     * @return lista de álbuns e HTTP 200
     */
    @Override
    @GetMapping(path = {"", "/findall"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        List<Resposta> resposta = albumService.findAll().stream().map(Resposta::de).toList();
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }

    /** Cadastra um álbum.
     * @param album dados do álbum
     * @return álbum criado e HTTP 201
     */
    @Override
    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Album album) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Resposta.de(albumService.save(album)));
    }

    /** Atualiza os dados de um álbum existente.
     * @param id identificador do álbum
     * @param album novos dados do álbum
     * @return álbum atualizado e HTTP 200
     */
    @Override
    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Album album) {
        return ResponseEntity.status(HttpStatus.OK).body(Resposta.de(albumService.update(id, album)));
    }

    /** Exclui um álbum.
     * @param id identificador do álbum
     * @return resposta sem corpo e HTTP 204
     */
    @Override
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /** Busca um álbum pelo identificador.
     * @param id identificador do álbum
     * @return álbum encontrado e HTTP 200
     */
    @Override
    @GetMapping(path = "/findbyid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(Resposta.de(albumService.findById(id)));
    }

    /** Pesquisa álbuns pelo nome.
     * @param nome trecho do nome a pesquisar
     * @return álbuns correspondentes e HTTP 200
     */
    @Override
    @GetMapping(path = "/findbyname/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByNome(@PathVariable String nome) {
        List<Resposta> resposta = albumService.findByNome(nome).stream().map(Resposta::de).toList();
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
}
