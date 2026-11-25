package br.com.ifba.album.repository;
import br.com.ifba.album.entity.Album;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AlbumRepository extends JpaRepository<Album, Long> { List<Album> findByNomeContainingIgnoreCase(String nome); }
