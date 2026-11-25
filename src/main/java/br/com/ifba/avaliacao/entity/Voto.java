package br.com.ifba.avaliacao.entity;

import br.com.ifba.usuario.entity.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "votos", uniqueConstraints = @UniqueConstraint(name = "uk_voto_usuario_avaliacao", columnNames = {"usuario_id", "avaliacao_id"}))
public class Voto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao;
    @Column(nullable = false)
    private boolean util;

    protected Voto() {}
    public Voto(Usuario usuario, Avaliacao avaliacao, boolean util) { this.usuario = usuario; this.avaliacao = avaliacao; this.util = util; }
}
