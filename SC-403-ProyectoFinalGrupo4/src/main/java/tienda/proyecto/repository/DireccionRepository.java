package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Integer> {

    public List<Direccion> findByActivoTrue();
}
