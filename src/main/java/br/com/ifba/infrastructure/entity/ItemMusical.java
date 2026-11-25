package br.com.ifba.infrastructure.entity;

import br.com.ifba.infrastructure.entity.TipoItem;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING, length = 20)
@Table(name = "itens_musicais", indexes = @Index(name = "idx_item_tipo_nome", columnList = "tipo,nome"))
public abstract class ItemMusical {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 180)
    private String nome;
    @Column(length = 180)
    private String artista;
    @Column(length = 1000)
    private String descricao;

    protected ItemMusical() {}
    protected ItemMusical(String nome, String artista, String descricao) {
        this.nome = nome; this.artista = artista; this.descricao = descricao;
    }
    public Long getId() { return id; }
    public abstract TipoItem getTipo();
    public String getNome() { return nome; }
    public String getArtista() { return artista; }
    public String getDescricao() { return descricao; }
    public void setNome(String nome) { this.nome = nome; }
    public void setArtista(String artista) { this.artista = artista; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void atualizarDados(String nome, String artista, String descricao) {
        this.nome = nome;
        this.artista = artista;
        this.descricao = descricao;
    }
}
