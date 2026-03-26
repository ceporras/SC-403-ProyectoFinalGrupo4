
package tienda.proyecto.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
//import javax.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    //ESTO ES PARA INTENTAR QUE EL CARRITO SIRVA EN TODA PAGINA
    /*@ModelAttribute("cartCount")
    public int getCartCount(HttpSession session) {
        // Logic to get your cart count. 
        // Example: if you store your cart in the Session
        List<?> cart = (List<?>) session.getAttribute("cart");
        return (cart != null) ? cart.size() : 0;
    }*/
}