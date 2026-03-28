package tienda.proyecto.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    public List<Rol> findByActivoTrue();
    public Optional<Rol> findByRol(String rol);
}