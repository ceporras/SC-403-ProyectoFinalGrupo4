
package tienda.proyecto.controller;

import java.util.List;
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
import tienda.proyecto.domain.Direccion;
import tienda.proyecto.domain.Pedido;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.service.CategoriaService;
import tienda.proyecto.service.PedidoService;
import tienda.proyecto.service.ProductoService;
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
    
    public Usuario getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        var usuario = usuarioService.getUsuarioPorUsername(username);
        return usuario.get();
    }
    
    //pagina de confirmar pedido
    @GetMapping("/{idPedido}/crear")
    public String confirmarPedido(@PathVariable("idPedido") Integer idPedido, Model model){
        
        Usuario usuario = getLoggedInUser();
        
        Pedido pedido = pedidoService.getPedidoByIdAndUsuario(idPedido, usuario);
        
        List<Direccion> direcciones = usuarioCuentaService.listarDirecciones(usuario);
        
        model.addAttribute("pedido", pedido);
        model.addAttribute("direcciones", direcciones);
        
        return "/pedido/confirmar_pedido";
    }
    
    
    @PostMapping("/confirmar")
    public String completarPedido(@RequestParam("idPedido") Integer idPedido, 
            @RequestParam("idDireccion") Integer idDireccion, @RequestParam("metodoPago") String metodoPago){
        Usuario usuario = getLoggedInUser();
        
        pedidoService.completarPedido(idPedido, usuario, idDireccion, metodoPago);
        
        return "redirect:/pedido/exito";
    }
}
