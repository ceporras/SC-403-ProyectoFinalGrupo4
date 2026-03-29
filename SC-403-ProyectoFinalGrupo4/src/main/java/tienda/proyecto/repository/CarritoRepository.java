
package tienda.proyecto.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.Carrito;
import tienda.proyecto.domain.Producto;
import tienda.proyecto.domain.Usuario;



public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    
    public Optional<Carrito> findByUsuarioAndProducto(Usuario usuario, Producto producto);
    
    public List<Carrito> findByUsuario(Usuario usuario);
    
    void deleteByUsuarioAndProducto(Usuario usuario, Producto producto);
    
}
