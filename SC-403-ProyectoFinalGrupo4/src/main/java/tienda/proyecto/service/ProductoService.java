package tienda.proyecto.service;

import tienda.proyecto.domain.Producto;
import tienda.proyecto.repository.ProductoRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tienda.proyecto.domain.Carrito;
import tienda.proyecto.domain.CartItem;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.repository.CarritoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private FirebaseStorageService firebaseStorageService;
    
    @Autowired
    private CarritoRepository carritoRepository;

    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activo) {
        if (activo) { //solo activos
            return productoRepository.findByActivoTrue();
        }
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {

        return productoRepository.findById(idProducto);

    }

    @Transactional(readOnly = true)
    public List<Producto> getProductoByCategoria(int idCategoria) {
        return productoRepository.getProductoByCategoria(idCategoria);
    }

    @Transactional
    public void save(Producto producto, MultipartFile imagenFile) {

        Producto existente = null;
        if (producto.getIdProducto() != null) {
            existente = productoRepository
                    .findById(producto.getIdProducto())
                    .orElse(null);
        }
        //si producto existe y ya tiene imagen en DB, no se reemplaza con null para conservar la imagen
        if (imagenFile.isEmpty() && existente != null) {
            producto.setRutaImagen(existente.getRutaImagen());
        }

        producto = productoRepository.save(producto);

        if (!imagenFile.isEmpty()) { //si no esta vacio, pasaron una imagen
            try {
                String rutaImagen = firebaseStorageService.uploadImage(imagenFile, "producto", producto.getIdProducto());
                producto.setRutaImagen(rutaImagen);
                productoRepository.save(producto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void delete(Integer idProducto) {
        //verifica si la producto existe antes de intentar eliminarlo
        if (!productoRepository.existsById(idProducto)) {
            //lanza exception para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("La producto con ID " + idProducto + " no existe.");
        }
        try {
            productoRepository.deleteById(idProducto);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la producto. Tiene datos asociados.", e);
        }
    }

    //remover luego
    public List<CartItem> productsInCart = new ArrayList<>();

    /*
    public void addToCart(Integer idProducto, int cantidad) {
        //buscar si existe en DB
        Optional<Producto> productoSearch = getProducto(idProducto);
        CartItem cartItem = new CartItem();
        try {
            cartItem.setProducto(productoSearch.get());
            cartItem.setCantidadCart(cantidad);

            //buscar si producto ya esta en carrito
            for (CartItem item : productsInCart) {
                if (item.getProducto().getIdProducto().equals(idProducto)) {
                    //producto ya existe en el carrito, modificar cantidad
                    item.setCantidadCart(item.getCantidadCart() + cantidad);
                    return;
                }
            }
            //producto no estaba en el carrito, agregar nuevo
            productsInCart.add(cartItem);

        } catch (Exception e) {
            throw e;
        }

    }*/
    
    
    public void addToCart(Usuario usuario, Producto producto, int cantidad){
        //validar si ya existe producto en el carrito
        Optional<Carrito> existe = carritoRepository.findByUsuarioAndProducto(usuario, producto);
        
        if (existe.isPresent()){
            Carrito cart = existe.get();
            //validar esta logica sobre bajar cantidad
            cart.setCantidad(cart.getCantidad()+ cantidad);
            //cart.setCantidad(cantidad);
            //guardar solo modificando cantidad
            carritoRepository.save(cart);
        }else{
            Carrito cart = new Carrito();
            cart.setUsuario(usuario);
            cart.setProducto(producto);
            cart.setCantidad(cantidad);
            carritoRepository.save(cart);
            
        }
        
    }

    /*public List<CartItem> getCarrito() {

        return productsInCart;
    }*/
    
    public List<Carrito> getCart(Usuario usuario){
        return carritoRepository.findByUsuario(usuario);
    }

    public void modificarCarrito(int idProducto, int cantidad){
        for (CartItem item : productsInCart) {
            if (item.getProducto().getIdProducto().equals(idProducto)) {
                item.setCantidadCart(cantidad);
                break;
            }
        }
    }
    
    public void elimiarItemCarrito(int idProducto) {
        productsInCart.removeIf(item
                -> item.getProducto().getIdProducto().equals(idProducto)
        );
    }
    
    public void cartCheckout(){
        //integrar con pedido
    }
}
