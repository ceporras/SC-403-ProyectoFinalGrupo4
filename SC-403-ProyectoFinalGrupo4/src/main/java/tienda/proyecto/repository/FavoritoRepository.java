
package tienda.proyecto.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.Favorito;
import tienda.proyecto.domain.Producto;
import tienda.proyecto.domain.Usuario;



public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {
    
    public Optional<Favorito> findByUsuarioAndProducto(Usuario usuario, Producto producto);
    
    public List<Favorito> findByUsuario(Usuario usuario);  
    
    public void deleteByUsuarioAndProducto(Usuario usuario, Producto producto);
    
}
