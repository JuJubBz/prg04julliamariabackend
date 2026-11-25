package br.com.ifba.banda.controller;

import br.com.ifba.banda.entity.Banda;
import org.springframework.http.ResponseEntity;

/** Contrato REST para operações de bandas. */
public interface BandaIController {
    ResponseEntity<?> findAll(String q);
    ResponseEntity<?> save(Banda banda);
    ResponseEntity<?> update(Long id, Banda banda);
    ResponseEntity<?> delete(Long id);
    ResponseEntity<?> findById(Long id);
    ResponseEntity<?> findByNome(String nome);
}
