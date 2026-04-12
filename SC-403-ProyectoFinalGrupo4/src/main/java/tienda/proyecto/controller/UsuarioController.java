package tienda.proyecto.controller;

import tienda.proyecto.domain.Usuario;
import tienda.proyecto.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tienda.proyecto.service.RolService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final MessageSource messageSource;
    private final RolService rolService;

    public UsuarioController(UsuarioService usuarioService,
            MessageSource messageSource, RolService rolService) {
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
        this.rolService = rolService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listado")
    public String inicio(Model model) {
        var usuarios = usuarioService.getUsuarios(false);
        var roles = rolService.getRoles();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("roles", roles);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "/usuario/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Usuario usuario,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Redirige al formulario de edición/creación para mostrar errores
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error04", null, Locale.getDefault()));
            // Si no hay idUsuario, redirige al listado con modal para agregar
            if (usuario.getIdUsuario() == null) {
                return "redirect:/usuario/listado";
            }
            // Si hay idUsuario, redirige al formulario de modificación
            return "redirect:/usuario/modificar/" + usuario.getIdUsuario();
        }
        usuarioService.save(usuario, true);
        redirectAttributes.addFlashAttribute("todoOk",
                messageSource.getMessage("mensaje.actualizado",
                        null, Locale.getDefault()));
        return "redirect:/usuario/listado";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idUsuario,
            RedirectAttributes redirectAttributes) {
        try {
            usuarioService.delete(idUsuario);
            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.eliminado", null,
                            Locale.getDefault()));
        } catch (IllegalArgumentException e) {
            // Captura argumento inválido para el mensaje de "no existe"
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error01", null,
                            Locale.getDefault()));
        } catch (IllegalStateException e) {
            // Captura estado ilegal para el mensaje de "datos asociados"
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error02", null,
                            Locale.getDefault()));
        } catch (NoSuchMessageException e) {
            // Captura cualquier otra excepción inesperada
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error03", null,
                            Locale.getDefault()));
        }
        return "redirect:/usuario/listado";
    }

    @GetMapping("/modificar/{idUsuario}")
    public String modificar(@PathVariable("idUsuario") Integer idUsuario,
            Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.getUsuario(idUsuario);
        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    "El usuario no fue encontrado.");
            return "redirect:/usuario/listado";
        }
        Usuario usuario = usuarioOpt.get();
        usuario.setPassword("");
        model.addAttribute("usuario", usuario);
        return "/usuario/modifica";
    }
    
    @GetMapping("/editar/{idUsuario}")
    public String editar(@PathVariable("idUsuario") Integer idUsuario,
            Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.getUsuario(idUsuario);
        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    "El usuario no fue encontrado.");
            return "redirect:/usuario/listado";
        }
        Usuario usuario = usuarioOpt.get();
        model.addAttribute("usuario", usuario);
        return "/usuario/modifica";
    }
}