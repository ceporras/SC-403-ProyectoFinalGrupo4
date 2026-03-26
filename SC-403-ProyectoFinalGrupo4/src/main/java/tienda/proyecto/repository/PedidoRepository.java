package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tienda.proyecto.domain.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("SELECT p FROM Pedido p WHERE p.direccion.usuario.idUsuario = :idUsuario")
    List<Pedido> obtenerPedidosPorUsuario(@Param("idUsuario") Integer idUsuario);
}