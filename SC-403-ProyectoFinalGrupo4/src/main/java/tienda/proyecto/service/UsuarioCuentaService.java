package tienda.proyecto.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tienda.proyecto.domain.Direccion;
import tienda.proyecto.domain.Pedido;
import tienda.proyecto.domain.Telefono;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.repository.DireccionRepository;
import tienda.proyecto.repository.PedidoRepository;
import tienda.proyecto.repository.TelefonoRepository;

@Service
public class UsuarioCuentaService {

    @Autowired
    private TelefonoRepository telefonoRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Telefono> listarTelefonos(Usuario usuario) {
        return telefonoRepository.obtenerTelefonosPorUsuario(usuario.getIdUsuario());
    }

    public void guardarTelefono(Telefono telefono) {
        telefonoRepository.save(telefono);
    }

    public List<Direccion> listarDirecciones(Usuario usuario) {
        return direccionRepository.obtenerDireccionesPorUsuario(usuario.getIdUsuario());
    }

    public void guardarDireccion(Direccion direccion) {
        direccionRepository.save(direccion);
    }

    /*public List<Pedido> listarPedidos(Usuario usuario) {
        return pedidoRepository.obtenerPedidosPorUsuario(usuario.getIdUsuario());
    }*/
    
    public List<Pedido> listarPedidos(Usuario usuario) {
        return pedidoRepository.findByUsuario(usuario);
    }
    
    
    public void eliminarTelefono(Integer idTelefono) {
    telefonoRepository.deleteById(idTelefono);
}

public void eliminarDireccion(Integer idDireccion) {
    direccionRepository.deleteById(idDireccion);
}
}