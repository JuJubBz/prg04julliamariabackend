package br.com.ifba.catalogo.dto;
import br.com.ifba.infrastructure.entity.ItemMusical;
import br.com.ifba.infrastructure.entity.TipoItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public final class ItemDtos {
    private ItemDtos() {}
    public record Criar(@NotNull TipoItem tipo,@NotBlank @Size(max=180) String nome,@Size(max=180) String artista,@Size(max=1000) String descricao) {}
    public record Resposta(Long id, TipoItem tipo, String nome, String artista, String descricao) {
        public static Resposta de(ItemMusical i) { return new Resposta(i.getId(), i.getTipo(), i.getNome(), i.getArtista(), i.getDescricao()); }
    }
}
