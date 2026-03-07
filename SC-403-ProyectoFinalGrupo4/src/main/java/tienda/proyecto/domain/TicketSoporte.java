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
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "ticket_soporte")

public class TicketSoporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket", nullable = false)
    private Integer idTicket;

    @Size(max = 30)
    @NotNull
    @Column(nullable = false)
    private String estado;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotNull
    private String descripcion;

    @NotNull
    @Column(nullable = false)
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;
}
