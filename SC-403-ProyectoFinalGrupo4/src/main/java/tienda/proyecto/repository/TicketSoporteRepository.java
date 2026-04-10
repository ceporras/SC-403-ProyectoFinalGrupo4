package tienda.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.TicketSoporte;

public interface TicketSoporteRepository extends JpaRepository<TicketSoporte, Integer> {
}