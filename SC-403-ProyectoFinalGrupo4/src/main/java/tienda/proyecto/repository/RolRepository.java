package tienda.proyecto.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    public Optional<Rol> findByRol(String rol);
}
