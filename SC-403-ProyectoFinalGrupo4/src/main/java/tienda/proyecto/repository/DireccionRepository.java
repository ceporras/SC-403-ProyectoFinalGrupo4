package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tienda.proyecto.domain.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Integer> {

    @Query("SELECT d FROM Direccion d WHERE d.usuario.id_usuario = :idUsuario")
    List<Direccion> obtenerDireccionesPorUsuario(@Param("idUsuario") Integer idUsuario);
}