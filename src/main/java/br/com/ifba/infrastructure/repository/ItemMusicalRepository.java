package br.com.ifba.infrastructure.repository;
import br.com.ifba.infrastructure.entity.ItemMusical;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ItemMusicalRepository extends JpaRepository<ItemMusical, Long> { List<ItemMusical> findByNomeContainingIgnoreCase(String nome); }
