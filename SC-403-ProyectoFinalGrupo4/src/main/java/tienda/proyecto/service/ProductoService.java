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
import tienda.proyecto.domain.Favorito;
import tienda.proyecto.domain.Usuario;
import tienda.proyecto.repository.CarritoRepository;
import tienda.proyecto.repository.FavoritoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private FavoritoRepository favoritoRepository;

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

    //metodos para carrito
    @Transactional
    public void addToCart(Usuario usuario, Producto producto, int cantidad) {
        //validar si ya existe producto en el carrito
        Optional<Carrito> existe = carritoRepository.findByUsuarioAndProducto(usuario, producto);

        if (existe.isPresent()) {
            Carrito cart = existe.get();
            cart.setCantidad(cart.getCantidad() + cantidad);
            //guardar solo modificando cantidad
            carritoRepository.save(cart);
        } else {
            Carrito cart = new Carrito();
            cart.setUsuario(usuario);
            cart.setProducto(producto);
            cart.setCantidad(cantidad);
            carritoRepository.save(cart);

        }

    }

    @Transactional(readOnly = true)
    public List<Carrito> getCart(Usuario usuario) {
        return carritoRepository.findByUsuario(usuario);
    }
    
    @Transactional(readOnly = true)
    public int cartCount(Usuario usuario) {
        return carritoRepository.countByUsuario(usuario);
    }

    @Transactional
    public void modificarCarrito(int idUsuario, int idProducto, int cantidad) {

        carritoRepository.updateCantidadByProductoId(cantidad, idProducto, idUsuario);

    }

    @Transactional
    public void elimiarItemCarrito(Usuario usuario, Producto producto) {
        carritoRepository.deleteByUsuarioAndProducto(usuario, producto);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarFiltrados(String textoBusqueda, Integer idCategoria) {

        if (textoBusqueda != null && textoBusqueda.trim().isEmpty()) {
            textoBusqueda = null;
        }
        return productoRepository.filtrarProductos(textoBusqueda, idCategoria);
    }

    //metodos para tratar con productos guardados como favoritos
    @Transactional
    public void addToFavorito(Usuario usuario, Producto producto) {

        //agregar a DB como favorito para el usuario
        Favorito fav = new Favorito();
        fav.setUsuario(usuario);
        fav.setProducto(producto);
        favoritoRepository.save(fav);

    }

    //solo 1 por usuario y id
    @Transactional(readOnly = true)
    public Optional<Favorito> getFavorito(Usuario usuario, Producto producto) {
        return favoritoRepository.findByUsuarioAndProducto(usuario, producto);
    }

    //lista de todos por usuario
    @Transactional(readOnly = true)
    public List<Favorito> getFavoritos(Usuario usuario) {
        return favoritoRepository.findByUsuario(usuario);
    }

    @Transactional
    public void elimiarFavorito(Usuario usuario, Producto producto) {
        favoritoRepository.deleteByUsuarioAndProducto(usuario, producto);
    }

}
