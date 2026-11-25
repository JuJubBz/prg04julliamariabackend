package br.com.ifba.artista.entity;

import br.com.ifba.infrastructure.entity.ItemMusical;
import br.com.ifba.infrastructure.entity.TipoItem;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ARTISTA")
public class Artista extends ItemMusical {
    protected Artista() {}
    public Artista(String nome, String descricao) { super(nome, null, descricao); }
    @Override public TipoItem getTipo() { return TipoItem.ARTISTA; }
}
