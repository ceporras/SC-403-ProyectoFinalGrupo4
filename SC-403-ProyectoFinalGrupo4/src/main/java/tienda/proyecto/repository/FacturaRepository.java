package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {

    public List<Factura> findByActivoTrue();
}
