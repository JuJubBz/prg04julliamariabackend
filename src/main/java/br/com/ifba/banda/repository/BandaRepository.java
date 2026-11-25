package br.com.ifba.banda.repository;
import br.com.ifba.banda.entity.Banda;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BandaRepository extends JpaRepository<Banda, Long> { List<Banda> findByNomeContainingIgnoreCase(String nome); }
