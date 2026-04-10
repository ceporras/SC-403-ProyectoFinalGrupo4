package tienda.proyecto.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tienda.proyecto.domain.Factura;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.repository.FacturaRepository;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Transactional(readOnly = true)
    public List<Factura> getAllFacturas() {
        return facturaRepository.findByActivoTrueOrderByFechaDesc();
    }
    
    @Transactional(readOnly = true)
    public List<Factura> getFacturasByUsuario(Usuario usuario) {
        return facturaRepository.findByPedidoUsuarioIdUsuario(usuario.getIdUsuario());
    }

    @Transactional(readOnly = true)
    public Factura getFacturaById(Integer idFactura) {
        return facturaRepository.findById(idFactura).orElse(null);
    }
}
