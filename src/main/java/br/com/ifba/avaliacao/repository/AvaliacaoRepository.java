package br.com.ifba.avaliacao.repository;
import br.com.ifba.avaliacao.entity.Avaliacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> { List<Avaliacao> findAllByOrderByPublicadaEmDesc(); List<Avaliacao> findByItemIdOrderByPublicadaEmDesc(Long itemId); }
