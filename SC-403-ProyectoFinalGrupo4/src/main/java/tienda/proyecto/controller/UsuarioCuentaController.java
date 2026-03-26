package tienda.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tienda.proyecto.domain.Direccion;
import tienda.proyecto.domain.Telefono;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.service.UsuarioCuentaService;

@Controller
@RequestMapping("/cuenta")
public class UsuarioCuentaController {

    @Autowired
    private UsuarioCuentaService service;

    @GetMapping
    public String verCuenta(Model model) {
        Integer idUsuario = 1;

        model.addAttribute("telefonos", service.listarTelefonos(idUsuario));
        model.addAttribute("direcciones", service.listarDirecciones(idUsuario));
        model.addAttribute("pedidos", service.listarPedidos(idUsuario));

        model.addAttribute("nuevoTelefono", new Telefono());
        model.addAttribute("nuevaDireccion", new Direccion());

        return "cuenta";
    }

    @PostMapping("/telefono")
    public String guardarTelefono(@ModelAttribute("nuevoTelefono") Telefono telefono,
                                  RedirectAttributes redirectAttributes) {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1); 

        telefono.setUsuario(usuario);
        telefono.setActivo(true);

        service.guardarTelefono(telefono);
        redirectAttributes.addFlashAttribute("mensaje", "Teléfono guardado correctamente.");

        return "redirect:/cuenta";
    }

    @PostMapping("/direccion")
    public String guardarDireccion(@ModelAttribute("nuevaDireccion") Direccion direccion,
                                   RedirectAttributes redirectAttributes) {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);

        direccion.setUsuario(usuario);
        direccion.setActivo(true);

        service.guardarDireccion(direccion);
        redirectAttributes.addFlashAttribute("mensaje", "Dirección guardada correctamente.");

        return "redirect:/cuenta";
    }
    
   @PostMapping("/telefono/eliminar/{id}")
public String eliminarTelefono(@PathVariable("id") Integer idTelefono) {
    service.eliminarTelefono(idTelefono);
    return "redirect:/cuenta";
}

@PostMapping("/direccion/eliminar/{id}")
public String eliminarDireccion(@PathVariable("id") Integer idDireccion) {
    service.eliminarDireccion(idDireccion);
    return "redirect:/cuenta";
}
}