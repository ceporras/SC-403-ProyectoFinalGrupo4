package tienda.proyecto.controller;

import tienda.proyecto.domain.Producto;
import tienda.proyecto.service.ProductoService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tienda.proyecto.domain.Carrito;
import tienda.proyecto.domain.Pedido;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.service.CategoriaService;
import tienda.proyecto.service.PedidoService;
import tienda.proyecto.service.UsuarioService;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/producto/listado";
    }

    //vista de un solo producto
    @GetMapping("/detalle/{idProducto}")
    public String detalle(Model model, @PathVariable("idProducto") int idProducto) {
        //sacar el objeto del Optional, de otro modo se sube el optional al UI en vez de Producto
        var producto = productoService.getProducto(idProducto).orElse(null);
        //si optional es null
        if (producto == null) {
            return "error/404";
        }

        model.addAttribute("producto", producto);
        return "/producto/detalle";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid /*@ModelAttribute*/ Producto producto, @RequestParam(/*value = "imagenFile", required = false*/) MultipartFile imagenFile, RedirectAttributes redirectAttributes) {
        productoService.save(producto, imagenFile);
        //redirectAttributes.addFlashAttribute("todoOk",messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));

        return "redirect:/producto/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idProducto, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            productoService.delete(idProducto);
        } catch (IllegalArgumentException e) {
            titulo = "error";//captura e de argumento invalido para el mensaje de "no existe"
            detalle = "producto.error01";
        } catch (IllegalStateException e) {
            titulo = "error";//captura e de estado ilegarl para mensaje de "datos asociados"
            detalle = "producto.error02";
        } catch (Exception e) {
            titulo = "error";//captura el resto de e
            detalle = "producto.error03";
        }
        //redirectAttributes.addFlashAttribute(titulo,messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/producto/listado";
    }

    @GetMapping("/modificar/{idProducto}")
    public String modificar(@PathVariable("idProducto") Integer idProducto, Model model, RedirectAttributes redirectAttributes) {
        Optional<Producto> productoOpt = productoService.getProducto(idProducto);
        if (productoOpt.isEmpty()) {
            //redirectAttributes.addFlashAttribute("error", messageSource.getMessage("producto.error01", null, Locale.getDefault()));
            return "redirect:/producto/listado";
        }
        model.addAttribute("producto", productoOpt.get());
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/producto/modifica";
    }

    public Usuario getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        var usuario = usuarioService.getUsuarioPorUsername(username);
        return usuario.get();
    }

    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam("idProducto") int idProducto,
            @RequestParam("cantidad") int cantidad) {

        Optional<Producto> productoOpt = productoService.getProducto(idProducto);
        Producto producto = productoOpt.get();

        //sacar usuario logueado para asignarle el carrito
        Usuario usuario = getLoggedInUser();
        productoService.addToCart(usuario, producto, cantidad);
        return "redirect:/producto/carrito";
    }

    @GetMapping("/carrito")
    public String carritoListado(Model model) {
        Usuario usuario = getLoggedInUser();
        var productosInCart = productoService.getCart(usuario);
        model.addAttribute("productosInCart", productosInCart);

        model.addAttribute("cartCount", productosInCart.size());
        return "/producto/carrito";
    }

    @PostMapping("/carrito/actualizar")
    public String modificarCarrito(@RequestParam Integer idProducto,
            @RequestParam int cantidad) {
        Usuario usuario = getLoggedInUser();
        
        productoService.modificarCarrito(usuario.getIdUsuario(), idProducto, cantidad);
        return "redirect:/producto/carrito";
    }

    @PostMapping("/carrito/eliminar")
    public String elimarDelCarrito(@RequestParam Integer idProducto) {
        Usuario usuario = getLoggedInUser();
        Producto producto = productoService.getProducto(idProducto).get();
        
        productoService.elimiarItemCarrito(usuario, producto);
        return "redirect:/producto/carrito";
    }

    @PostMapping("/carrito/checkout")
    public String checkout(Model model) {
        //sacar usuario logueado para pasarlo a pedido
        Usuario usuario = getLoggedInUser();
        Pedido pedido = pedidoService.crearPedido(usuario);
        return "redirect:/pedido/"+ pedido.getIdPedido() +"/crear";
    }

}
