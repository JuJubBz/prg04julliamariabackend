package br.com.ifba.banda.entity;

import br.com.ifba.infrastructure.entity.ItemMusical;
import br.com.ifba.infrastructure.entity.TipoItem;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BANDA")
public class Banda extends ItemMusical {
    protected Banda() {}
    public Banda(String nome, String descricao) { super(nome, null, descricao); }
    @Override public TipoItem getTipo() { return TipoItem.BANDA; }
}
