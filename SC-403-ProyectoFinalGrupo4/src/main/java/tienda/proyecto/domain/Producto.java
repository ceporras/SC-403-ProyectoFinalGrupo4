package tienda.proyecto.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "producto")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;
    //private Integer idCategoria; no se usa porque se crea en una relacion

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El nombre no puede estar vacía.")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres.")
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column
    @NotNull(message = "El stock no puede estar vacío.")
    private int stock;
    
    @Column(precision = 12, scale = 2)
    @NotNull(message = "El precio no puede estar vacío.")
    @DecimalMin(value = "0.01", inclusive = true, message = "El precio debe ser mayor a 0.")
    private BigDecimal precio;

    @NotNull(message = "El campo de existencias no puede estar vacío.")
    @Min(value = 0, message = "Las existencias deben ser un número mayor o igual a 0.")
    private Integer existencias;

    @Column(name = "ruta_imagen", length = 1024)
    private String rutaImagen;
    
    private boolean activo;
    
    //relacion a categoria
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    
    //detalle pedido
    @OneToMany(mappedBy = "producto")
    private List<DetallePedido> detallePedido;
    
}
