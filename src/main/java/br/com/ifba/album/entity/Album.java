package br.com.ifba.album.entity;

import br.com.ifba.infrastructure.entity.ItemMusical;
import br.com.ifba.infrastructure.entity.TipoItem;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ALBUM")
public class Album extends ItemMusical {
    protected Album() {}
    public Album(String nome, String artista, String descricao) { super(nome, artista, descricao); }
    @Override public TipoItem getTipo() { return TipoItem.ALBUM; }
}
