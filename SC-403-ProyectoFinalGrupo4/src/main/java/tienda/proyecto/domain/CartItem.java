package tienda.proyecto.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {

    private Producto producto;
    private int cantidadCart;

    public CartItem() {
    }

    public CartItem(Producto producto, int cantidadCart) {
        this.producto = producto;
        this.cantidadCart = cantidadCart;
    }

    public boolean searchByIdProducto(int id) {

        if (producto.getIdProducto() == id) {
            return true;
        } else {
            return false;
        }
    }

}
