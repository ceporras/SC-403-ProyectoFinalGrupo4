
package tienda.proyecto.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import tienda.proyecto.domain.Carrito;
import tienda.proyecto.domain.Producto;
import tienda.proyecto.domain.Usuario;



public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    
    public Optional<Carrito> findByUsuarioAndProducto(Usuario usuario, Producto producto);
    
    public List<Carrito> findByUsuario(Usuario usuario);
    
    public int countByUsuario(Usuario usuario);
    
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE carrito SET cantidad=:cantidad WHERE id_producto=:idProducto AND id_usuario=:idUsuario")
    public void updateCantidadByProductoId(@Param("cantidad") int cantidad, 
            @Param("idProducto") int idProducto, @Param("idUsuario") int idUsuario);
    
    
    public void deleteByUsuarioAndProducto(Usuario usuario, Producto producto);
    
}
