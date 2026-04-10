
package tienda.proyecto.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tienda.proyecto.domain.Pedido;
import tienda.proyecto.domain.TicketSoporte;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.repository.PedidoRepository;
import tienda.proyecto.repository.TicketSoporteRepository;

@Service
public class TicketSoporteService {

    @Autowired
    private TicketSoporteRepository ticketRepository;

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Transactional
    public void crearNuevoTicket(Integer idPedido, Usuario usuario, String descripcion){
        //buscar pedido al que asociar ticket, segun usuario logueado
        Pedido pedido = pedidoRepository.findByPedidoAndUsuario(idPedido, usuario.getIdUsuario());
        //crear ticket
        TicketSoporte ticket = new TicketSoporte();
        ticket.setDescripcion(descripcion);
        ticket.setEstado("PENDIENTE");
        ticket.setActivo(true);
        ticket.setUsuario(usuario);
        ticket.setPedido(pedido);
        
        ticketRepository.save(ticket);
    }
    //encontrar todods los tickets de un usuario especifico, para cliente
    public List<TicketSoporte> getTicketsByUsuario(Usuario usuario) {
        return ticketRepository.findByUsuario(usuario);
    }
    //todos, para admin
    public List<TicketSoporte> getAllTickets() {
        return ticketRepository.findAll();
    }
    //especifico, para admin
    public TicketSoporte getTicketById(Integer id) {
        return ticketRepository.findById(id).orElse(null);
    }
}
