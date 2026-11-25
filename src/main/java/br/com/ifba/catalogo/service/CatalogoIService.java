package br.com.ifba.catalogo.service;

import br.com.ifba.catalogo.dto.ItemDtos.Criar;
import br.com.ifba.catalogo.dto.ItemDtos.Resposta;
import br.com.ifba.infrastructure.entity.TipoItem;
import java.util.List;

/** Contrato de operações agregadas sobre todos os tipos de item musical. */
public interface CatalogoIService {
    List<Resposta> pesquisar(TipoItem tipo, String q);
    Resposta criar(Criar dados);
}
