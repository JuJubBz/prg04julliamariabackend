package br.com.ifba.avaliacao.service;

import br.com.ifba.avaliacao.dto.AvaliacaoDtos.Atualizar;
import br.com.ifba.avaliacao.dto.AvaliacaoDtos.Criar;
import br.com.ifba.avaliacao.dto.AvaliacaoDtos.Resposta;
import br.com.ifba.avaliacao.dto.AvaliacaoDtos.VotoRequest;
import java.util.List;

/** Contrato de regras de negócio para avaliações e votos. */
public interface AvaliacaoIService {
    List<Resposta> listar(Long itemId);
    Resposta buscar(Long id);
    Resposta criar(Criar dados, String email);
    Resposta atualizar(Long id, Atualizar dados, String email);
    void excluir(Long id, String email);
    void votar(Long id, VotoRequest dados, String email);
    void removerVoto(Long id, String email);
}
