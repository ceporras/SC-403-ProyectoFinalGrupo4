package tienda.proyecto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaPedido;

    @NotNull
    @Column(nullable = false)
    @Size(max = 30)
    private String estado;

    //relacion a tabla intermedia
    @OneToMany(mappedBy = "pedido")
    private List<DetallePedido> detallePedido;

    @OneToMany(mappedBy = "pedido")
    private List<Factura> factura;

    //una direccion tiene varios pedidos
    @ManyToOne
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @OneToMany(mappedBy = "pedido")
    private List<TicketSoporte> ticketSoporte;

    @NotNull
    @Column(nullable = false)
    private Boolean activo;
}
