package br.com.ifba.album.controller;

import br.com.ifba.album.entity.Album;
import org.springframework.http.ResponseEntity;

/** Contrato HTTP do recurso de álbuns. */
public interface AlbumIController {
    ResponseEntity<?> findAll();
    ResponseEntity<?> save(Album album);
    ResponseEntity<?> update(Long id, Album album);
    ResponseEntity<?> delete(Long id);
    ResponseEntity<?> findById(Long id);
    ResponseEntity<?> findByNome(String nome);
}
