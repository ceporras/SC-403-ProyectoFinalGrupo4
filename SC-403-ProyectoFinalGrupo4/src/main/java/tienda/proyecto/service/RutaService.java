
package tienda.proyecto.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tienda.proyecto.domain.Ruta;
import tienda.proyecto.repository.RutaRepository;

@Service
public class RutaService {

    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }
    
    @Transactional(readOnly=true)
    public List<Ruta> getRutas(){
        return rutaRepository.findAllByOrderByRequiereRolAsc();
    }
}