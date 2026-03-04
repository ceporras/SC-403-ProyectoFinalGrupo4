
package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.TicketSoporte;

public interface TicketSoporteRepository extends JpaRepository<TicketSoporte, Integer>{
    public List<TicketSoporte> findByActivoTrue();
}
