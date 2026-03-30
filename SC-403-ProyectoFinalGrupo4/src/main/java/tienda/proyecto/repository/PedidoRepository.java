package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tienda.proyecto.domain.Factura;
import tienda.proyecto.domain.Pedido;
import tienda.proyecto.domain.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    //@Query("SELECT p FROM pedido p WHERE p.direccion.usuario.idUsuario = :idUsuario")
    //List<Pedido> obtenerPedidosPorUsuario(@Param("idUsuario") Integer idUsuario);
    
    @Query(nativeQuery = true, value = "SELECT * FROM pedido WHERE  id_usuario=:idUsuario")
    List<Pedido> obtenerPedidosPorUsuario(@Param("idUsuario") Integer idUsuario);
    
    //@Query(nativeQuery = true, value = "")
    //public List<Pedido> findByUsuario(Integer idUsuario);
    public List<Pedido> findByUsuario(Usuario usuario);
    
    @Query(nativeQuery = true, value = "SELECT * FROM pedido WHERE id_pedido=:idPedido AND id_usuario=:idUsuario")
    public Pedido findByPedidoAndUsuario(@Param("idPedido")Integer idPedido, @Param("idUsuario")Integer idUsuario);
    
    //public Pedido findByPedidoAndUsuario(Integer idPedido, Integer idUsuario);
    
}