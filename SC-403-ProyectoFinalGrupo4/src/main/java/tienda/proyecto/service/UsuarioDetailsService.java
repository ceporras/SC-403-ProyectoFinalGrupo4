
package tienda.proyecto.service;

import jakarta.servlet.http.HttpSession;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tienda.proyecto.repository.UsuarioRepository;
import tienda.proyecto.domain.Usuario;
import java.util.Optional;



@Service("userDetailsService")
public class UsuarioDetailsService implements UserDetailsService{
    
    private final UsuarioRepository usuarioRepository;
    private final HttpSession session;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository, HttpSession session) {
        this.usuarioRepository = usuarioRepository;
        this.session = session;
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException{
     
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: "+username));
        
        //cargar roles de usuario y generarlos como roles de seguridad
        var roles = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_"+rol.getRol()))
                .collect(Collectors.toSet());
        
        //retornar el user con su info
        return new User(usuario.getUsername(),usuario.getPassword(), roles);
    }
    
    
}