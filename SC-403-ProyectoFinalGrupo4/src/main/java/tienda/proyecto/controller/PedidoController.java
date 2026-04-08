package tienda.proyecto.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tienda.proyecto.domain.DetallePedido;
import tienda.proyecto.domain.Direccion;
import tienda.proyecto.domain.Pedido;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.service.PedidoService;
import tienda.proyecto.service.UsuarioCuentaService;
import tienda.proyecto.service.UsuarioService;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioCuentaService usuarioCuentaService;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(@RequestParam(required = false) String estado, Model model) {
        var pedidos = pedidoService.getPedidos(estado);
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("estadosPedido", pedidoService.getEstadosPedido());
        model.addAttribute("estadoSeleccionado", estado == null ? "" : estado);
        return "/pedido/listado";
    }

    public Usuario getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        var usuario = usuarioService.getUsuarioPorUsername(username);
        return usuario.get();
    }

    //pagina de confirmar pedido
    @GetMapping("/{idPedido}/crear")
    public String confirmarPedido(@PathVariable("idPedido") Integer idPedido, Model model) {

        Usuario usuario = getLoggedInUser();

        Pedido pedido = pedidoService.getPedidoByIdAndUsuario(idPedido, usuario);

        List<Direccion> direcciones = usuarioCuentaService.listarDirecciones(usuario);

        List<DetallePedido> itemsPedido = new ArrayList<>();
        itemsPedido = pedido.getDetallePedido();

        model.addAttribute("itemsPedido", itemsPedido);
        model.addAttribute("pedido", pedido);
        model.addAttribute("direcciones", direcciones);

        return "/pedido/confirmar_pedido";
    }

    @PostMapping("/confirmar_pedido")
    public String completarPedido(@RequestParam("idPedido") Integer idPedido,
            @RequestParam("idDireccion") Integer idDireccion, @RequestParam("metodoPago") String metodoPago) {
        Usuario usuario = getLoggedInUser();

        pedidoService.completarPedido(idPedido, usuario, idDireccion, metodoPago);

        return "/pedido/pedido_creado";
    }

    @GetMapping("/detalle/{idPedido}")
    public String detalle(Model model, @PathVariable("idPedido") int idPedido) {
        Usuario usuario = getLoggedInUser();
        var pedido = pedidoService.getPedidoByIdAndUsuario(idPedido, usuario);

        //sumar total 
        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedido d : pedido.getDetallePedido()) {
            total = total.add(
                    d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())));
        }

        model.addAttribute("total", total);

        model.addAttribute("pedido", pedido);
        return "/pedido/detalle";
    }

    @PostMapping("/guardarEstado")
    public String guardarEstado(@RequestParam Integer idPedido,
            @RequestParam String estado,
            @RequestParam(required = false) String estadoSeleccionado,
            RedirectAttributes redirectAttributes) {
        try {
            pedidoService.actualizarEstado(idPedido, estado);
            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("pedido.estado.actualizado", null, Locale.getDefault()));
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("pedido.error01", null, Locale.getDefault()));
        }

        if (estadoSeleccionado != null && !estadoSeleccionado.isBlank()) {
            return "redirect:/pedido/listado?estado=" + estadoSeleccionado;
        }
        return "redirect:/pedido/listado";
    }
}
