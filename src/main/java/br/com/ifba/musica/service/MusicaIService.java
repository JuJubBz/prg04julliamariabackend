package br.com.ifba.musica.service;

import br.com.ifba.musica.entity.Musica;
import java.util.List;

/** Contrato de regras de negócio para músicas. */
public interface MusicaIService {
    List<Musica> findAll(String q);
    Musica save(Musica musica);
    Musica update(Long id, Musica musica);
    void delete(Long id);
    Musica findById(Long id);
    List<Musica> findByNome(String nome);
}
