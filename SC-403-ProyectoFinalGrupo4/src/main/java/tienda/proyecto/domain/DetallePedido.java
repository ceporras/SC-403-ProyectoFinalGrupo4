
package tienda.proyecto.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_pedido")
public class DetallePedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_pedido")
    private Integer idDetallePedido;
    
    @Column(name = "precio_unitario", nullable = false)
    @NotNull
    private BigDecimal precioUnitario;
    
    @NotNull
    @Column(nullable = false)
    private int cantidad;
    
    @Column(nullable = false)
    @NotNull
    private Boolean activo;
    
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
    
}
