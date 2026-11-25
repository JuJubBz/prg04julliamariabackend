package br.com.ifba.album.service;

import br.com.ifba.album.entity.Album;
import java.util.List;

/** Define as operações de negócio disponíveis para o módulo de álbuns. */
public interface AlbumIService {
    Album save(Album album);
    List<Album> findAll();
    Album update(Long id, Album album);
    void delete(Long id);
    Album findById(Long id);
    List<Album> findByNome(String nome);
}
