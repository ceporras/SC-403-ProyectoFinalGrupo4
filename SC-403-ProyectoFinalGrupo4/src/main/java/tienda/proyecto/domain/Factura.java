package tienda.proyecto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "factura")
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    @NotNull
    private Integer idFactura;
    
    @NotNull
    @Column(nullable = false)
    private BigDecimal montoTotal;
    
    @NotNull
    @Column(nullable = false)
    private LocalDate fecha;
    
    @NotNull
    @Column(nullable = false)
    private Boolean activo;
    
    //una sola factura por pedido 1:1
    @OneToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;
}
