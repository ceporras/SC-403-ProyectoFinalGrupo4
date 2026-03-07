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

    @NotNull
    @Column(nullable = false, length = 50)
    @NotBlank(message = "El nombre no puede estar vacía.")
    @Size(max = 150, message = "El nombre no puede tener más de 150 caracteres.")
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    @NotNull(message = "El stock no puede estar vacío.")
    @Min(value = 0, message = "Las existencias deben ser un número mayor o igual a 0.")
    private int stock;
    
    @Column(precision = 12, scale = 2)
    @NotNull(message = "El precio no puede estar vacío.")
    @DecimalMin(value = "0.01", inclusive = true, message = "El precio debe ser mayor a 0.")
    private BigDecimal precio;

    @Size(max = 1024)
    @Column(name = "imagen_url", length = 1024)
    private String rutaImagen;
    
    @Size(max = 255)
    @Column(name = "notas_salida", length = 255)
    private String notasSalida;
    
    @Size(max = 255)
    @Column(name = "notas_corazon", length = 255)
    private String notasCorazon;
    
    @Size(max = 255)
    @Column(name = "notas_fondo", length = 255)
    private String notasFondo;

    //relacion a categoria
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    
    //detalle pedido
    @OneToMany(mappedBy = "producto")
    private List<DetallePedido> detallePedido;
    
    //relacion a reviews
    @OneToMany(mappedBy = "producto")
    private List<Review> review;
    
    @NotNull
    @Column(nullable = false)
    private Boolean activo;
    
}
