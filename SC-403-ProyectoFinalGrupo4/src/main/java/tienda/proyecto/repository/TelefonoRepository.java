
package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.Telefono;


public interface TelefonoRepository extends JpaRepository<Telefono, Integer>{
    public List<Telefono> findByActivoTrue();
}
