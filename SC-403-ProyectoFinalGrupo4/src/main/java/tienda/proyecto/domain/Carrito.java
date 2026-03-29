
package tienda.proyecto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;


@Data
@Entity
@Table(name = "carrito", uniqueConstraints = @UniqueConstraint(
        columnNames = {"id_usuario","id_producto"}))
public class Carrito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Integer idCarrito;
    
    @NotNull
    @Column(nullable = false, name = "cantidad")
    private int cantidad;
    
    /*@NotNull
    @Column(nullable = false, name = "id_usuario")
    private int idUsuario;
    
    @NotNull
    @Column(nullable = false, name = "id_producto")
    private int idProducto;*/
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
}
