package br.com.ifba.artista.repository;
import br.com.ifba.artista.entity.Artista;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ArtistaRepository extends JpaRepository<Artista, Long> { List<Artista> findByNomeContainingIgnoreCase(String nome); }
