package br.com.ifba.banda.service;

import br.com.ifba.banda.entity.Banda;
import java.util.List;

/** Contrato de regras de negócio para bandas. */
public interface BandaIService {
    List<Banda> findAll(String q);
    Banda save(Banda banda);
    Banda update(Long id, Banda banda);
    void delete(Long id);
    Banda findById(Long id);
    List<Banda> findByNome(String nome);
}
