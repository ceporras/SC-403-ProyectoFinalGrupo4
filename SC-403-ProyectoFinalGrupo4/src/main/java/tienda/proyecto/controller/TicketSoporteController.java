package tienda.proyecto.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tienda.proyecto.domain.TicketSoporte;
import tienda.proyecto.repository.TicketSoporteRepository;

@Controller
@RequestMapping("/ticket")
public class TicketSoporteController {

    @Autowired
    private TicketSoporteRepository ticketSoporteRepository;

    @GetMapping("/listado")
    public String listado(Model model) {
        List<TicketSoporte> tickets = ticketSoporteRepository.findAll();
        model.addAttribute("tickets", tickets);
        return "soporte/listado";
    }

    @GetMapping("/nuevo")
    public String nuevoTicket(Model model) {
        model.addAttribute("ticket", new TicketSoporte());
        return "soporte/nuevo";
    }

    @PostMapping("/guardar")
    public String guardarTicket(@ModelAttribute TicketSoporte ticket) {
        ticket.setActivo(true);
        ticket.setEstado("Pendiente");
        ticket.setUsuario(null);
        ticket.setPedido(null);
        ticketSoporteRepository.save(ticket);
        return "redirect:/ticket/listado";
    }

    @GetMapping("/detalle/{idTicket}")
    public String verDetalle(@PathVariable Integer idTicket, Model model) {
        TicketSoporte ticket = ticketSoporteRepository.findById(idTicket).orElse(null);

        if (ticket == null) {
            return "redirect:/ticket/listado";
        }

        model.addAttribute("ticket", ticket);
        return "soporte/detalle";
    }
}