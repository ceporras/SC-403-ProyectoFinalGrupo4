
package tienda.proyecto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    public List<Usuario> findByActivoTrue();   
}
