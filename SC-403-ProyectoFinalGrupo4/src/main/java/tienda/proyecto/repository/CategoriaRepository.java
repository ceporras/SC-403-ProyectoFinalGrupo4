
package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tienda.proyecto.domain.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    public List<Categoria> findByActivoTrue();
}
