package tienda.proyecto.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tienda.proyecto.domain.Direccion;
import tienda.proyecto.domain.Pedido;
import tienda.proyecto.domain.Telefono;
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

    public List<Telefono> listarTelefonos(Integer idUsuario) {
        return telefonoRepository.obtenerTelefonosPorUsuario(idUsuario);
    }

    public void guardarTelefono(Telefono telefono) {
        telefonoRepository.save(telefono);
    }

    public List<Direccion> listarDirecciones(Integer idUsuario) {
        return direccionRepository.obtenerDireccionesPorUsuario(idUsuario);
    }

    public void guardarDireccion(Direccion direccion) {
        direccionRepository.save(direccion);
    }

    public List<Pedido> listarPedidos(Integer idUsuario) {
        return pedidoRepository.obtenerPedidosPorUsuario(idUsuario);
    }
    public void eliminarTelefono(Integer idTelefono) {
    telefonoRepository.deleteById(idTelefono);
}

public void eliminarDireccion(Integer idDireccion) {
    direccionRepository.deleteById(idDireccion);
}
}