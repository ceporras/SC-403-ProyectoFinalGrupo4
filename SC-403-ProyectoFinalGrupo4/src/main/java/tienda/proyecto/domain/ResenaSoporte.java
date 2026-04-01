package tienda.proyecto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "resena_soporte")
public class ResenaSoporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena_soporte")
    private Integer idResenaSoporte;

    @Max(value = 5)
    @Min(value = 1)
    @NotNull
    @Column(nullable = false)
    private Integer calificacion;

    @Size(max = 500)
    @Column(length = 500)
    private String comentario;

    @NotNull
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(nullable = false)
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "id_ticket", nullable = false)
    private TicketSoporte ticketSoporte;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}