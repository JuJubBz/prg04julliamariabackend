package br.com.ifba.avaliacao.repository;
import br.com.ifba.avaliacao.entity.Voto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VotoRepository extends JpaRepository<Voto, Long> { Optional<Voto> findByUsuarioIdAndAvaliacaoId(Long usuarioId, Long avaliacaoId); long countByAvaliacaoIdAndUtil(Long avaliacaoId, boolean util); void deleteAllByAvaliacaoId(Long avaliacaoId); }
