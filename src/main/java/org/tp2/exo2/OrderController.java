package org.tp2.exo2;



import org.tp2.exo2.Order;
import org.tp2.exo2.OrderService;

public class OrderController {

    private final OrderService orderService;

    // Injection de dépendance par constructeur
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Point d'entrée pour la création d'une commande.
     * Délègue le traitement à l'OrderService.
     * @param order la commande à créer
     */
    public void createOrder(Order order) {
        orderService.createOrder(order);
    }
}