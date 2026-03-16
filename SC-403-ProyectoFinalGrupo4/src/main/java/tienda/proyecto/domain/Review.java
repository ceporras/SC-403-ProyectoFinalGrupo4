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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Cesar Porras
 */

@Getter
@Setter
@Data
@Entity
@Table(name = "resena")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena")
    private Integer idReview;
    
    @Max(value = 5)
    @Min(value = 1)
    @NotNull
    @Column(nullable = false)
    private int calificacion;
    
    private String comentario;

    @Size(max = 1024)
    @Column(name = "imagen_url", length = 1024)
    private String rutaImagen;
    
    @NotNull
    @Column(nullable = false)
    private LocalDate fecha;
    
    @Column(nullable = false)
    private Boolean activo;
    
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
