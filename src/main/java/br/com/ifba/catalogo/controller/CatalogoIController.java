package br.com.ifba.catalogo.controller;

import br.com.ifba.catalogo.dto.ItemDtos.Criar;
import br.com.ifba.infrastructure.entity.TipoItem;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

/** Contrato REST para consultas e criação agregadas do catálogo. */
public interface CatalogoIController {
    ResponseEntity<?> findAll(TipoItem tipo, String q);
    ResponseEntity<?> save(Criar dados, Authentication auth);
}
