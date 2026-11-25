package br.com.ifba.banda.service;

import br.com.ifba.banda.entity.Banda;
import br.com.ifba.banda.repository.BandaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service @RequiredArgsConstructor @Transactional
public class BandaService implements BandaIService {
    private final BandaRepository repository;
    @Override @Transactional(readOnly=true) public List<Banda> findAll(String q) { return repository.findByNomeContainingIgnoreCase(q == null ? "" : q.trim()); }
    @Override public Banda save(Banda banda) { validar(banda); return repository.save(banda); }
    @Override public Banda update(Long id, Banda banda) {
        validar(banda); Banda atual=findById(id);
        atual.atualizarDados(banda.getNome().trim(), null, banda.getDescricao()); return repository.save(atual);
    }
    @Override public void delete(Long id) { repository.delete(findById(id)); }
    @Override @Transactional(readOnly=true) public Banda findById(Long id) {
        validarId(id); return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Banda não encontrada."));
    }
    @Override @Transactional(readOnly=true) public List<Banda> findByNome(String nome) {
        if (nome == null || nome.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o nome da banda.");
        return repository.findByNomeContainingIgnoreCase(nome.trim());
    }
    private void validar(Banda banda) {
        if (banda == null || banda.getNome() == null || banda.getNome().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome da banda é obrigatório.");
    }
    private void validarId(Long id) {
        if (id == null || id <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O identificador da banda é inválido.");
    }
}
