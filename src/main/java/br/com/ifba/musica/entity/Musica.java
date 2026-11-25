package br.com.ifba.musica.entity;

import br.com.ifba.infrastructure.entity.ItemMusical;
import br.com.ifba.infrastructure.entity.TipoItem;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MUSICA")
public class Musica extends ItemMusical {
    protected Musica() {}
    public Musica(String nome, String artista, String descricao) { super(nome, artista, descricao); }
    @Override public TipoItem getTipo() { return TipoItem.MUSICA; }
}
