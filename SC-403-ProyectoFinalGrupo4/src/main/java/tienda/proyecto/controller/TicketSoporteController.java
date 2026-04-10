package tienda.proyecto.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tienda.proyecto.domain.TicketSoporte;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.service.TicketSoporteService;
import tienda.proyecto.service.UsuarioService;

@Controller
@RequestMapping("/ticket")
public class TicketSoporteController {

    @Autowired
    private TicketSoporteService ticketSoporteService;
    
    @Autowired
    private UsuarioService usuarioService;

    //sacar usuario logueado
    private Usuario getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return usuarioService.getUsuarioPorUsername(auth.getName()).get();
    }
    
    @GetMapping("/listado")
    public String listado(Model model) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        List<TicketSoporte> tickets;
        
        if(isAdmin){
            //mostar todos los tickets
            tickets = ticketSoporteService.getAllTickets();
        }else{
            //mostrar tickets solo del usuario logueado
            Usuario usuario = getLoggedInUser();
            tickets = ticketSoporteService.getTicketsByUsuario(usuario);
        }
        
        model.addAttribute("tickets", tickets);
        return "soporte/listado";
    }

    //ticket es creado segun un pedido especifico
    @GetMapping("/nuevo/{idPedido}")
    public String nuevoTicket(@PathVariable Integer idPedido,Model model) {
        model.addAttribute("idPedido",idPedido);
        return "soporte/nuevo";
    }

    @PostMapping("/guardar")
    public String guardarTicket(@RequestParam Integer idPedido, @RequestParam String descripcion) {
        //obtener usuario logueado para pasarle al service al guardar
        //asi se asocia el ticket a un usuario especifico
        Usuario usuario = getLoggedInUser();
        
        ticketSoporteService.crearNuevoTicket(idPedido, usuario, descripcion);
        return "redirect:/ticket/listado";
    }

    @GetMapping("/detalle/{idTicket}")
    public String verDetalle(@PathVariable Integer idTicket, Model model) {
        TicketSoporte ticket = ticketSoporteService.getTicketById(idTicket);

        if (ticket == null) {
            return "redirect:/ticket/listado";
        }

        model.addAttribute("ticket", ticket);
        return "soporte/detalle";
    }
}