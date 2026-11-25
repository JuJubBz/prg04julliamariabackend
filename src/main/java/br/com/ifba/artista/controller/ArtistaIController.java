package br.com.ifba.artista.controller;

import br.com.ifba.artista.entity.Artista;
import org.springframework.http.ResponseEntity;

/** Contrato REST para operações de artistas. */
public interface ArtistaIController {
    ResponseEntity<?> findAll(String q);
    ResponseEntity<?> save(Artista artista);
    ResponseEntity<?> update(Long id, Artista artista);
    ResponseEntity<?> delete(Long id);
    ResponseEntity<?> findById(Long id);
    ResponseEntity<?> findByNome(String nome);
}
