
package tienda.proyecto.repository;

import tienda.proyecto.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface ProductoRepository extends JpaRepository<Producto, Integer>{
    public List<Producto> findByActivoTrue();
    
    //filtrar productos por categoria    
    @Query(nativeQuery = true, value = "SELECT * FROM producto WHERE id_categoria=:id_categoria")
    public List<Producto> getProductoByCategoria(@Param("id_categoria") int idCategoria);
}
