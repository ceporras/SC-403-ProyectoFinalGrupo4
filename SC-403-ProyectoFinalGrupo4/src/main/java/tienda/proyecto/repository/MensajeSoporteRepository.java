package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.MensajeSoporte;

public interface MensajeSoporteRepository extends JpaRepository<MensajeSoporte, Integer> {

    List<MensajeSoporte> findByTicketSoporteIdTicket(Integer idTicket);
}
