package br.com.ifba.avaliacao.entity;

import br.com.ifba.infrastructure.entity.ItemMusical;
import br.com.ifba.usuario.entity.Usuario;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 2000)
    private String texto;
    @Column(nullable = false, updatable = false)
    private Instant publicadaEm = Instant.now();
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario autor;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "item_id", nullable = false)
    private ItemMusical item;

    protected Avaliacao() {}
    public Avaliacao(String texto, Usuario autor, ItemMusical item) { this.texto = texto; this.autor = autor; this.item = item; }
    public Long getId() { return id; }
    public String getTexto() { return texto; }
    public Instant getPublicadaEm() { return publicadaEm; }
    public Usuario getAutor() { return autor; }
    public ItemMusical getItem() { return item; }
    public void atualizar(String texto) { this.texto = texto; }
}
