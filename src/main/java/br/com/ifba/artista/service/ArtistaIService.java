package br.com.ifba.artista.service;

import br.com.ifba.artista.entity.Artista;
import java.util.List;

/** Contrato de regras de negócio para artistas. */
public interface ArtistaIService {
    List<Artista> findAll(String q);
    Artista save(Artista artista);
    Artista update(Long id, Artista artista);
    void delete(Long id);
    Artista findById(Long id);
    List<Artista> findByNome(String nome);
}
