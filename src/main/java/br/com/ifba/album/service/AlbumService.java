package br.com.ifba.album.service;

import br.com.ifba.album.entity.Album;
import br.com.ifba.album.repository.AlbumRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/** Implementa as regras de negócio do módulo de álbuns. */
@Service
@RequiredArgsConstructor
@Transactional
public class AlbumService implements AlbumIService {
    private final AlbumRepository albumRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Album> findAll() {
        return albumRepository.findAll();
    }

    @Override
    public Album save(Album album) {
        validar(album);
        return albumRepository.save(album);
    }

    @Override
    public Album update(Long id, Album album) {
        if (album == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os dados do álbum são obrigatórios.");
        }
        Album existente = findById(id);
        validar(album);
        existente.atualizarDados(album.getNome().trim(), album.getArtista(), album.getDescricao());
        return albumRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        albumRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Album findById(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O identificador do álbum é inválido.");
        }
        return albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Álbum não encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Album> findByNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um nome para pesquisar.");
        }
        return albumRepository.findByNomeContainingIgnoreCase(nome.trim());
    }

    private void validar(Album album) {
        if (album == null || album.getNome() == null || album.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do álbum é obrigatório.");
        }
    }
}
