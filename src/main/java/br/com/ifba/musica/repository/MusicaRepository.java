package br.com.ifba.musica.repository;
import br.com.ifba.musica.entity.Musica;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MusicaRepository extends JpaRepository<Musica, Long> { List<Musica> findByNomeContainingIgnoreCase(String nome); }
