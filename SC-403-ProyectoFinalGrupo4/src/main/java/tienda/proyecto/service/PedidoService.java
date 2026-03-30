package tienda.proyecto.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tienda.proyecto.domain.*;
import tienda.proyecto.repository.*;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    @Autowired
    private CarritoRepository carritoRepository;
    @Autowired
    private DireccionRepository direccionRepository;

    /*
    @Transactional(readOnly = true)
    public List<Pedido> getPedidos() {

    }*/
    @Transactional(readOnly = true)
    public Pedido getPedidoByIdAndUsuario(int idPedido, Usuario usuario) {
        return pedidoRepository.findByPedidoAndUsuario(idPedido, usuario.getIdUsuario());
    }

    @Transactional
    public Pedido crearPedido(Usuario usuario) {
        //buscar items de carrito segun usuario logueado
        List<Carrito> itemsCarrito = carritoRepository.findByUsuario(usuario);

        if (itemsCarrito.isEmpty()) {
            throw new RuntimeException("No se puede crear el pedido. Carrito vacio");
        }

        //crear pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFechaPedido(LocalDate.now());
        pedido.setEstado("PENDIENTE");
        pedido.setActivo(true);

        List<DetallePedido> detallesPedido = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        //por cada item del carrito, crear detalle y agregarlo a array
        for (Carrito item : itemsCarrito) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getProducto().getPrecio());
            detalle.setActivo(true);

            detallesPedido.add(detalle);

            //sumar a precio total
            BigDecimal subtotal = item.getProducto().getPrecio()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));

            total = total.add(subtotal);
        }

        pedido.setDetallePedido(detallesPedido);

        
        pedidoRepository.save(pedido);
        
        //borrar carrito, ya que se crea pedido
        carritoRepository.deleteAll(itemsCarrito);
        return pedido;
    }

    @Transactional
    public void completarPedido(Integer idPedido, Usuario usuario, Integer idDireccion, String metodoPago) {

        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow();

        if (!pedido.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new RuntimeException("Pedido no pertenece al usuario");
        }

        Direccion direccion = direccionRepository.findById(idDireccion).orElseThrow();
        pedido.setDireccion(direccion);

        //sumar total de la compra
        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedido d : pedido.getDetallePedido()) {

            BigDecimal subtotal = d.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(d.getCantidad()));

            total = total.add(subtotal);

            //reducir inventario segun cantidad comprada
            Producto p = d.getProducto();
            if (p.getStock() < d.getCantidad()) {
                throw new RuntimeException("Stock insuficiente");
            }

            p.setStock(p.getStock() - d.getCantidad());
            productoRepository.save(p);
        }

        //crear factura
        Factura factura = new Factura();
        factura.setPedido(pedido);
        factura.setActivo(true);
        factura.setFecha(LocalDate.now());
        factura.setMontoTotal(total);
        pedido.setFactura(factura);

        pedido.setEstado("CONFIRMADO");
        pedidoRepository.save(pedido);

    }
}
