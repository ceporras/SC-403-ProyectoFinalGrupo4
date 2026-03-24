package tienda.proyecto.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotNull
    @Column(length = 50, nullable = false, name = "username", unique = true)
    private String username;
    
    @NotNull
    @Email
    @Column(length = 50, nullable = false, name = "email", unique = true)
    private String email;
    
    @NotNull
    @Column(length = 50, nullable = false, name = "nombre")
    private String nombre;

    @NotNull
    @Column(length = 100, nullable = false, name = "primer_apellido")
    private String primerApellido;

    @Column(length = 100, nullable = true, name = "segundo_apellido")
    private String segundoApellido;

    @NotNull
    @Size(max = 255)
    @Column(length = 255, nullable = false, name = "contrasena")
    private String password;

    
    @NotNull
    @Column(nullable = false)
    private Boolean activo;
    
    @OneToMany(mappedBy = "usuario")
    private List<Direccion> direccion;
    
    //un usuario puede tener varios reviews
    @OneToMany(mappedBy = "usuario")
    private List<Review> review;
    
    @OneToMany(mappedBy = "usuario")
    private List<Telefono> telefono;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name = "usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private Set<Rol> roles = new HashSet<>();
}
