package br.com.ifba.musica.service;

import br.com.ifba.musica.entity.Musica;
import br.com.ifba.musica.repository.MusicaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service @RequiredArgsConstructor @Transactional
public class MusicaService implements MusicaIService {
    private final MusicaRepository repository;
    @Override @Transactional(readOnly=true) public List<Musica> findAll(String q) { return repository.findByNomeContainingIgnoreCase(q == null ? "" : q.trim()); }
    @Override public Musica save(Musica musica) { validar(musica); return repository.save(musica); }
    @Override public Musica update(Long id, Musica musica) {
        validar(musica); Musica atual=findById(id);
        atual.atualizarDados(musica.getNome().trim(), musica.getArtista(), musica.getDescricao()); return repository.save(atual);
    }
    @Override public void delete(Long id) { repository.delete(findById(id)); }
    @Override @Transactional(readOnly=true) public Musica findById(Long id) {
        validarId(id); return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Música não encontrada."));
    }
    @Override @Transactional(readOnly=true) public List<Musica> findByNome(String nome) {
        if (nome == null || nome.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o nome da música.");
        return repository.findByNomeContainingIgnoreCase(nome.trim());
    }
    private void validar(Musica musica) {
        if (musica == null || musica.getNome() == null || musica.getNome().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome da música é obrigatório.");
    }
    private void validarId(Long id) {
        if (id == null || id <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O identificador da música é inválido.");
    }
}
