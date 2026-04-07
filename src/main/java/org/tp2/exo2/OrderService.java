package org.tp2.exo2;



import org.tp2.exo2.OrderDao;
import org.tp2.exo2.Order;

public class OrderService {

    private final OrderDao orderDao;

    // Injection de dépendance par constructeur
    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    /**
     * Traite la logique métier puis délègue la persistance au DAO.
     * @param order la commande à créer
     */
    public void createOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("La commande ne peut pas être null");
        }
        // Logique métier (validation, calcul, etc.)
        orderDao.saveOrder(order);
    }
}