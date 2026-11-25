package br.com.ifba.avaliacao.dto;

import br.com.ifba.avaliacao.entity.Avaliacao;
import br.com.ifba.infrastructure.entity.TipoItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public final class AvaliacaoDtos {
    private AvaliacaoDtos() {}
    public record Criar(@NotBlank @Size(max=2000) String texto, @NotNull Long itemId) {}
    public record Atualizar(@NotBlank @Size(max=2000) String texto) {}
    public record VotoRequest(@NotNull Boolean util) {}
    public record Resposta(Long id, String texto, Instant publicadaEm, Long autorId, String autor, Long itemId, TipoItem tipoItem, String itemNome, long votosUteis, long votosNaoUteis) {
        public static Resposta de(Avaliacao a, long uteis, long naoUteis) {
            return new Resposta(a.getId(), a.getTexto(), a.getPublicadaEm(), a.getAutor().getId(), a.getAutor().getNome(), a.getItem().getId(), a.getItem().getTipo(), a.getItem().getNome(), uteis, naoUteis);
        }
    }
}
