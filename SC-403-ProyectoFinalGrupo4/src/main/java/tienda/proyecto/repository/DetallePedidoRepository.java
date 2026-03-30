package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tienda.proyecto.domain.DetallePedido;
import tienda.proyecto.domain.Pedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {

    public List<DetallePedido> findByActivoTrue();
    
    //public List<DetallePedido> findByIdPedido();
    @Query(nativeQuery = true, value = "SELECT * FROM detalle_pedido WHERE id_pedido=:idPedido")
    List<Pedido> findByIdPedido(@Param("idPedido") Integer idPedido);
}
