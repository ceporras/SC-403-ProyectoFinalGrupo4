
package tienda.proyecto.service;

import tienda.proyecto.domain.Producto;
import tienda.proyecto.repository.ProductoRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private FirebaseStorageService firebaseStorageService;
    
    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activo){
        if (activo) { //solo activos
            return productoRepository.findByActivoTrue();
        }
        return productoRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto){
        return productoRepository.findById(idProducto);
    }
    
    @Transactional
    public void save(Producto producto, MultipartFile imagenFile){
        producto = productoRepository.save(producto);
        if (!imagenFile.isEmpty()) { //si no esta vacio, pasaron una imagen
            try{
                String rutaImagen = firebaseStorageService.uploadImage(imagenFile, "producto", producto.getIdProducto());
                producto.setRutaImagen(rutaImagen);
                productoRepository.save(producto);
            }catch(IOException e){
                
            }
        }
    }
    
    @Transactional
    public void delete(Integer idProducto){
        //verifica si la producto existe antes de intentar eliminarlo
        if (!productoRepository.existsById(idProducto)){
            //lanza exception para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("La producto con ID " + idProducto + " no existe.");
        }
        try {
            productoRepository.deleteById(idProducto);
        }catch(DataIntegrityViolationException e){
            throw new IllegalStateException("No se puede eliminar la producto. Tiene datos asociados.", e);
        }
    }
}
