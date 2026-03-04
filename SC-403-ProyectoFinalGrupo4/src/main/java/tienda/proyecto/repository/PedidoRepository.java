package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    public List<Pedido> findByActivoTrue();
}
