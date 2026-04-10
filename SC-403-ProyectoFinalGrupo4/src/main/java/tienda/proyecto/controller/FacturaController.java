package tienda.proyecto.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.domain.Factura;
import tienda.proyecto.service.FacturaService;
import tienda.proyecto.service.UsuarioService;

@Controller
@RequestMapping("/factura")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private UsuarioService usuarioService;

    //sacar usuario logueado
    private Usuario getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return usuarioService.getUsuarioPorUsername(username).get();
    }

    @GetMapping("/listado")
    public String listado(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //determinar si el usuario actual es admin o no
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Factura>  facturas;
        
        if (isAdmin) {
            //sacar todas las facturas, sin importar el usuario
            facturas = facturaService.getAllFacturas(); 
        } else {
            //sacar solo facturas de usuario logueado
            Usuario usuario = getLoggedInUser();
            facturas = facturaService.getFacturasByUsuario(usuario);
        }
        model.addAttribute("facturas", facturas);
        return "/factura/listado";
    }

}
