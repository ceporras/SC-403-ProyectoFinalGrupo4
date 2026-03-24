package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.Ruta;

public interface RutaRepository extends JpaRepository<Ruta, Integer> {

    public List<Ruta> findAllByOrderByRequiereRolAsc();
}
