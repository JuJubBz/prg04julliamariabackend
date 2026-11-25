package br.com.ifba.catalogo.service;

import br.com.ifba.catalogo.dto.ItemDtos.Resposta;
import br.com.ifba.catalogo.dto.ItemDtos.Criar;
import br.com.ifba.album.entity.Album;
import br.com.ifba.artista.entity.Artista;
import br.com.ifba.banda.entity.Banda;
import br.com.ifba.infrastructure.entity.ItemMusical;
import br.com.ifba.infrastructure.entity.TipoItem;
import br.com.ifba.infrastructure.repository.ItemMusicalRepository;
import br.com.ifba.musica.entity.Musica;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CatalogoService implements CatalogoIService {
    private final ItemMusicalRepository itens;
    @Override @Transactional(readOnly=true)
    public List<Resposta> pesquisar(TipoItem tipo, String q) {
        String termo=q==null?"":q.trim();
        List<ItemMusical> resultado=itens.findByNomeContainingIgnoreCase(termo).stream()
                .filter(item -> tipo == null || item.getTipo() == tipo).toList();
        return resultado.stream().map(Resposta::de).toList();
    }
    @Override public Resposta criar(Criar dados) {
        String nome=dados.nome().trim();
        ItemMusical item=switch (dados.tipo()) {
            case ALBUM -> new Album(nome,dados.artista(),dados.descricao());
            case ARTISTA -> new Artista(nome,dados.descricao());
            case BANDA -> new Banda(nome,dados.descricao());
            case MUSICA -> new Musica(nome,dados.artista(),dados.descricao());
        };
        return Resposta.de(itens.save(item));
    }
}
