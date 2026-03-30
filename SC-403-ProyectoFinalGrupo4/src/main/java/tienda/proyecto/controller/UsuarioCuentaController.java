package tienda.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tienda.proyecto.domain.Direccion;
import tienda.proyecto.domain.Telefono;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.service.UsuarioCuentaService;
import tienda.proyecto.service.UsuarioService;

@Controller
@RequestMapping("/cuenta")
public class UsuarioCuentaController {

    @Autowired
    private UsuarioCuentaService usuarioCuentaService;

   @Autowired
    private UsuarioService usuarioService;
   
    public Usuario getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        var usuario = usuarioService.getUsuarioPorUsername(username);
        return usuario.get();
    }
    
    @GetMapping
    public String verCuenta(Model model) {
        Usuario usuario = getLoggedInUser();

        model.addAttribute("telefonos", usuarioCuentaService.listarTelefonos(usuario));
        model.addAttribute("direcciones", usuarioCuentaService.listarDirecciones(usuario));
        model.addAttribute("pedidos", usuarioCuentaService.listarPedidos(usuario));

        model.addAttribute("nuevoTelefono", new Telefono());
        model.addAttribute("nuevaDireccion", new Direccion());

        return "cuenta";
    }

    @PostMapping("/telefono")
    public String guardarTelefono(@ModelAttribute("nuevoTelefono") Telefono telefono,
                                  RedirectAttributes redirectAttributes) {

        Usuario usuario = getLoggedInUser();
        telefono.setUsuario(usuario);
        telefono.setActivo(true);

        usuarioCuentaService.guardarTelefono(telefono);
        redirectAttributes.addFlashAttribute("mensaje", "Teléfono guardado correctamente.");

        return "redirect:/cuenta";
    }

    @PostMapping("/direccion")
    public String guardarDireccion(@ModelAttribute("nuevaDireccion") Direccion direccion,
                                   RedirectAttributes redirectAttributes) {

        Usuario usuario = getLoggedInUser();
        direccion.setUsuario(usuario);
        direccion.setActivo(true);

        usuarioCuentaService.guardarDireccion(direccion);
        redirectAttributes.addFlashAttribute("mensaje", "Dirección guardada correctamente.");

        return "redirect:/cuenta";
    }
    
   @PostMapping("/telefono/eliminar/{id}")
public String eliminarTelefono(@PathVariable("id") Integer idTelefono) {
    usuarioCuentaService.eliminarTelefono(idTelefono);
    return "redirect:/cuenta";
}

@PostMapping("/direccion/eliminar/{id}")
public String eliminarDireccion(@PathVariable("id") Integer idDireccion) {
    usuarioCuentaService.eliminarDireccion(idDireccion);
    return "redirect:/cuenta";
}
}