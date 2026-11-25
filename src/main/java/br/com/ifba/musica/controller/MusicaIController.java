package br.com.ifba.musica.controller;

import br.com.ifba.musica.entity.Musica;
import org.springframework.http.ResponseEntity;

/** Contrato REST para operações de músicas. */
public interface MusicaIController {
    ResponseEntity<?> findAll(String q);
    ResponseEntity<?> save(Musica musica);
    ResponseEntity<?> update(Long id, Musica musica);
    ResponseEntity<?> delete(Long id);
    ResponseEntity<?> findById(Long id);
    ResponseEntity<?> findByNome(String nome);
}
