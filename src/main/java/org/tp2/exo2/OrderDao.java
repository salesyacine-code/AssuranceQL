package org.tp2.exo2;



import org.tp2.exo2.Order;

public interface OrderDao {
    /**
     * Enregistre une commande dans la base de données.
     * @param order la commande à persister
     */
    void saveOrder(Order order);
}
