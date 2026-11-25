package br.com.ifba.avaliacao.service;

import br.com.ifba.avaliacao.dto.AvaliacaoDtos.*;
import br.com.ifba.avaliacao.entity.Avaliacao;
import br.com.ifba.avaliacao.entity.Voto;
import br.com.ifba.avaliacao.repository.AvaliacaoRepository;
import br.com.ifba.avaliacao.repository.VotoRepository;
import br.com.ifba.infrastructure.entity.ItemMusical;
import br.com.ifba.infrastructure.repository.ItemMusicalRepository;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AvaliacaoService implements AvaliacaoIService {
    private final AvaliacaoRepository avaliacoes; private final UsuarioRepository usuarios; private final ItemMusicalRepository itens; private final VotoRepository votos;
    @Override @Transactional(readOnly=true) public List<Resposta> listar(Long itemId) {
        List<Avaliacao> lista=itemId==null?avaliacoes.findAllByOrderByPublicadaEmDesc():avaliacoes.findByItemIdOrderByPublicadaEmDesc(itemId);
        return lista.stream().map(this::resposta).toList();
    }
    @Override @Transactional(readOnly=true) public Resposta buscar(Long id) { return resposta(avaliacao(id)); }
    @Override @Transactional public Resposta criar(Criar d, String email) {
        Usuario autor=usuario(email); ItemMusical item=itens.findById(d.itemId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Item musical não encontrado"));
        return resposta(avaliacoes.save(new Avaliacao(d.texto().trim(),autor,item)));
    }
    @Override @Transactional public Resposta atualizar(Long id, Atualizar d, String email) {
        Avaliacao a=avaliacao(id); autoriza(a,email); a.atualizar(d.texto().trim()); return resposta(a);
    }
    @Override @Transactional public void excluir(Long id, String email) { Avaliacao a=avaliacao(id); autoriza(a,email); votos.deleteAllByAvaliacaoId(id); avaliacoes.delete(a); }
    @Override @Transactional public void votar(Long id, VotoRequest d, String email) {
        Avaliacao a=avaliacao(id); Usuario u=usuario(email);
        if(votos.findByUsuarioIdAndAvaliacaoId(u.getId(),id).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT,"Usuário já votou nesta avaliação");
        votos.save(new Voto(u,a,d.util()));
    }
    @Override @Transactional public void removerVoto(Long id, String email) {
        Usuario u=usuario(email); Voto v=votos.findByUsuarioIdAndAvaliacaoId(u.getId(),id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Voto não encontrado")); votos.delete(v);
    }
    private Resposta resposta(Avaliacao a) { return Resposta.de(a,votos.countByAvaliacaoIdAndUtil(a.getId(),true),votos.countByAvaliacaoIdAndUtil(a.getId(),false)); }
    private Avaliacao avaliacao(Long id) { return avaliacoes.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Avaliação não encontrada")); }
    private Usuario usuario(String email) { return usuarios.findByEmailIgnoreCase(email).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuário autenticado não encontrado")); }
    private void autoriza(Avaliacao a,String email) { if(!a.getAutor().getEmail().equalsIgnoreCase(email)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Somente o autor pode alterar esta avaliação"); }
}
