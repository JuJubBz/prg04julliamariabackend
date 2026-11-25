package br.com.ifba.artista.service;

import br.com.ifba.artista.entity.Artista;
import br.com.ifba.artista.repository.ArtistaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service @RequiredArgsConstructor @Transactional
public class ArtistaService implements ArtistaIService {
    private final ArtistaRepository repository;
    @Override @Transactional(readOnly=true) public List<Artista> findAll(String q) { return repository.findByNomeContainingIgnoreCase(q == null ? "" : q.trim()); }
    @Override public Artista save(Artista artista) { validar(artista); return repository.save(artista); }
    @Override public Artista update(Long id, Artista artista) {
        validar(artista); Artista atual=findById(id);
        atual.atualizarDados(artista.getNome().trim(), null, artista.getDescricao()); return repository.save(atual);
    }
    @Override public void delete(Long id) { repository.delete(findById(id)); }
    @Override @Transactional(readOnly=true) public Artista findById(Long id) {
        validarId(id); return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista não encontrado."));
    }
    @Override @Transactional(readOnly=true) public List<Artista> findByNome(String nome) {
        if (nome == null || nome.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o nome do artista.");
        return repository.findByNomeContainingIgnoreCase(nome.trim());
    }
    private void validar(Artista artista) {
        if (artista == null || artista.getNome() == null || artista.getNome().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do artista é obrigatório.");
    }
    private void validarId(Long id) {
        if (id == null || id <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O identificador do artista é inválido.");
    }
}
