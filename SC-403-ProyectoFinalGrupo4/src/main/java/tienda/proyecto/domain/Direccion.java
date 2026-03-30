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
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "direccion")
public class Direccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer idDireccion;

    @Column(name = "codigo_postal",nullable = false)
    @Min(value = 10000, message = "Numero no es un codigo postal valido")
    @Max(value = 99999, message = "Numero no es un codigo postal valido")
    private Integer codigoPostal;

    @NotNull
    @Size(max = 80)
    @Column(nullable = false)
    private String provincia;

    @NotNull
    @Size(max = 80)
    @Column(nullable = false)
    private String canton;
    
    @NotNull
    @Size(max = 80)
    @Column(nullable = false)
    private String distrito;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String direccionExacta;

    @NotNull
    @Column(nullable = false)
    private Boolean activo;
    
    //relacion a pedidos
    @OneToMany(mappedBy = "direccion")
    private List<Pedido> pedido;
    
    //un usuario tiene 1:n direcciones
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}
