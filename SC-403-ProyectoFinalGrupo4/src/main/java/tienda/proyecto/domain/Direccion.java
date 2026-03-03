
package tienda.proyecto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Direccion implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer id_direccion;
    
    @Column(name = "codigo_postal")
    @Min(value = 10000, message = "Numero no es un codigo postal valido")
    @Max(value = 99999, message = "Numero no es un codigo postal valido")
    private Integer codigoPostal;
    
    
    private String provincia;
    
    
    private String canton;
    
    
    private String distrito;
    
    private String direccionExacta;
}
