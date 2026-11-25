package br.com.ifba.avaliacao.controller;

import br.com.ifba.avaliacao.dto.AvaliacaoDtos.Atualizar;
import br.com.ifba.avaliacao.dto.AvaliacaoDtos.Criar;
import br.com.ifba.avaliacao.dto.AvaliacaoDtos.VotoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

/** Contrato REST para avaliações e votos. */
public interface AvaliacaoIController {
    ResponseEntity<?> findAll(Long itemId);
    ResponseEntity<?> findById(Long id);
    ResponseEntity<?> save(Criar dados, Authentication auth);
    ResponseEntity<?> update(Long id, Atualizar dados, Authentication auth);
    ResponseEntity<?> delete(Long id, Authentication auth);
    ResponseEntity<?> vote(Long id, VotoRequest dados, Authentication auth);
    ResponseEntity<?> removeVote(Long id, Authentication auth);
}
