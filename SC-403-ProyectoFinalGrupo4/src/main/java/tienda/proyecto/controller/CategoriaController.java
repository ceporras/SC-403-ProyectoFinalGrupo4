
package tienda.proyecto.controller;

import tienda.proyecto.domain.Categoria;
import tienda.proyecto.service.CategoriaService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping("/listado")
    public String inicio(Model model){
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);
        return "/categoria/listado";
    }
    
    @Autowired
    private MessageSource messageSource;
    
    @PostMapping("/guardar")
    public String guardar(@Valid Categoria categoria, RedirectAttributes redirectAttributes){
        categoriaService.save(categoria);
        //redirectAttributes.addFlashAttribute("todoOk",messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        return "redirect:/categoria/listado";
    }
    
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idCategoria, RedirectAttributes redirectAttributes){
        String titulo="todoOk";
        String detalle="mensaje.eliminado";
        try{
            categoriaService.delete(idCategoria);
        }catch (IllegalArgumentException e){
            titulo ="error";//captura e de argumento invalido para el mensaje de "no existe"
            detalle="categoria.error01";
        }catch (IllegalStateException e){
            titulo ="error";//captura e de estado ilegarl para mensaje de "datos asociados"
            detalle="categoria.error02";
        }catch (Exception e){
            titulo ="error";//captura el resto de e
            detalle="categoria.error03";
        }
        //redirectAttributes.addFlashAttribute(titulo,messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/categoria/listado";
    }
    
    @GetMapping("/modificar/{idCategoria}")
    public String modificar(@PathVariable("idCategoria") Integer idCategoria, Model model, RedirectAttributes redirectAttributes){
        Optional<Categoria> categoriaOpt = categoriaService.getCategoria(idCategoria);
        if (categoriaOpt.isEmpty()){
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("categoria.error01", null, Locale.getDefault()));
            return "redirect:/categoria/listado";
        }
        model.addAttribute("categoria",categoriaOpt.get());
        return "/categoria/modifica";
    }
}
