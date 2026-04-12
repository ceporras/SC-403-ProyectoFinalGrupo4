package tienda.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.service.ProductoService;
import tienda.proyecto.service.UsuarioService;
//import javax.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        //este metodo es para tener un count del carrito globalmente en todos 
        //los controllers, para mostrar un icono de cuantos items hay en carrito
        Usuario usuario = getLoggedInUser();

        if (usuario != null) {
            int cartCount = productoService.cartCount(usuario);
            model.addAttribute("cartCount", cartCount);
        } else {
            model.addAttribute("cartCount", 0);
        }
    }

    //obtener usuario logueado
    public Usuario getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //si el controller advice no recibe usuario al no estar logueado, va a fallar 
        //por eso retornar un null si no estoy logueado
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        String username = auth.getName();
        return usuarioService.getUsuarioPorUsername(username).orElse(null);
    }
    
    //Para que errores del PreAuthorize se manden a la pagina 403 en vez de 500
    @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
    public String handleAccessDenied() {
        return "error/403";
    }
}
