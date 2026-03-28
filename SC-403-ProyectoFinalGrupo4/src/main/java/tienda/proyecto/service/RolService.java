package tienda.proyecto.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tienda.proyecto.domain.Rol;
import tienda.proyecto.repository.RolRepository;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<Rol> getRoles() {
        return rolRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Rol> getByRol(String rol) {
        return rolRepository.findByRol(rol);
    }
}
