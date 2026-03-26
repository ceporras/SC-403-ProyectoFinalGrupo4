package tienda.proyecto.service;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tienda.proyecto.domain.Rol;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.repository.RolRepository;
import tienda.proyecto.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios(boolean activo) {
        if (activo) {
            return usuarioRepository.findByActivoTrue();
        }
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorUsernameYPassword(String username, String password) {
        return usuarioRepository.findByUsernameAndPassword(username, password);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorUsernameOCorreo(String username, String correo) {
        return usuarioRepository.findByUsernameOrEmail(username, correo);
    }

    @Transactional(readOnly = true)
    public boolean existeUsuarioPorUsernameOCorreo(String username, String correo) {
        return usuarioRepository.existsByUsernameOrEmail(username, correo);
    }

    @Transactional
    public void save(Usuario usuario, boolean encriptaClave) {
        //verificar si correo ya existe, en todos los users
        final Integer idUser = usuario.getIdUsuario();

        //busco el correo del usuario pasado al save
        Optional<Usuario> usuarioDuplicado = usuarioRepository.findByUsernameOrEmail(null, usuario.getEmail());

        //si encuentro un usuario con el correo, jalo info del usuario de la busqueda
        if (usuarioDuplicado.isPresent()) {
            Usuario encontrado = usuarioDuplicado.get();

            if (idUser == null || !encontrado.getIdUsuario().equals(idUser)) {
                throw new DataIntegrityViolationException("El correo ya esta en uso por otro usuario");
            }
        }

        //Se valida si la clave se va actualizar o si es un usuario nuevo se debe actualizar...
        var asignarRol = false;
        if (usuario.getIdUsuario() == null) {
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                throw new IllegalArgumentException("La contraseña es obligatoria para nuevos usuarios.");
            }
            //La primera vez como es activación no se encripta...
            usuario.setPassword(encriptaClave ? passwordEncoder.encode(usuario.getPassword()) : usuario.getPassword());
            asignarRol = true;
        } else {
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                // El campo de password en el formulario viene vacío (no se desea actualizar).
                // Recuperamos la contraseña HASHED existente de la base de datos.
                Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario())
                        .orElseThrow(() -> new IllegalArgumentException("Usuario a modificar no encontrado."));

                // Asignamos la contraseña existente al objeto "usuario" antes de guardarlo.                
                usuario.setPassword(encriptaClave ? passwordEncoder.encode(usuarioExistente.getPassword()) : usuarioExistente.getPassword());
            } else {
                // El campo de password NO está vacío (se desea actualizar).
                // Se encripta y se guarda la nueva contraseña.
                usuario.setPassword(encriptaClave ? passwordEncoder.encode(usuario.getPassword()) : usuario.getPassword());
            }
        }
        
        //Si se está creando el usuario, se crea el rol por defecto "USER"
        if (asignarRol) {// todo usuario creado tiene rol USUARIO, solo admins pueden crear otros admins
            Rol rol = rolRepository.findByRol("USUARIO")//admin como test
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            usuario.getRoles().add(rol);
            
            
        }
        //System.out.println("!!!!!!!!!!!!!!!!! rol asignado: " + usuario.getRoles().size());
        
        //validar que el usuario exista en el form pasado por el controller
        if (usuario.getIdUsuario()!=null){
            //creo var con usuario buscado del DB segun ID pasado por model
            Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario())
                        .orElseThrow(() -> new IllegalArgumentException("Usuario a modificar no encontrado."));
            
            //preservar roles que no son provistos por el controller model
            //de no hacer esto, se deshace la relacion hacia el rol
            usuario.setRoles(usuarioExistente.getRoles());
        }
        usuario = usuarioRepository.save(usuario);
    }

    @Transactional
    public void delete(Integer idUsuario) {
        // Verifica si la categoría existe antes de intentar eliminarlo
        if (!usuarioRepository.existsById(idUsuario)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException(
                    "El usuario con ID " + idUsuario + " no existe.");
        }
        try {
            usuarioRepository.deleteById(idUsuario);
        } catch (DataIntegrityViolationException e) {
            // Excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException(
                    "No se puede eliminar el usuario. Tiene datos asociados.", e);
        }
    }

    @Transactional
    public Usuario asignarRolPorUsername(String username, String rolStr) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado: " + username);
        }
        Usuario usuario = usuarioOpt.get();
        Optional<Rol> rolOpt = rolRepository.findByRol(rolStr);
        if (rolOpt.isEmpty()) {
            throw new RuntimeException("Rol no encontrado.");
        }
        Rol rol = rolOpt.get();
        usuario.getRoles().add(rol);
        return usuarioRepository.save(usuario);
    }

}