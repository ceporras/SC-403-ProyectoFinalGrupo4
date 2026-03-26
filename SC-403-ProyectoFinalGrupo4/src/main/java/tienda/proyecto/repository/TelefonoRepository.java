package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tienda.proyecto.domain.Telefono;

public interface TelefonoRepository extends JpaRepository<Telefono, Integer> {

    @Query("SELECT t FROM Telefono t WHERE t.usuario.id_usuario = :idUsuario")
    List<Telefono> obtenerTelefonosPorUsuario(@Param("idUsuario") Integer idUsuario);
}