package tienda.proyecto.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tienda.proyecto.domain.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    public Optional<Usuario> findByUsernameAndActivoTrue(String username);  
    
    public List<Usuario> findByActivoTrue();
    
    public Optional<Usuario> findByUsername(String username);  
    
    public Optional<Usuario> findByUsernameAndPassword(String username,String Password);  
    
    public Optional<Usuario> findByUsernameOrEmail(String username,String correo);  
    
    public boolean existsByUsernameOrEmail(String username, String correo);  
    
}