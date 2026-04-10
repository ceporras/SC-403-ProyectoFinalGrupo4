package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.ResenaSoporte;

public interface ResenaSoporteRepository extends JpaRepository<ResenaSoporte, Integer> {

    List<ResenaSoporte> findByTicketSoporteIdTicket(Integer idTicket);
}
