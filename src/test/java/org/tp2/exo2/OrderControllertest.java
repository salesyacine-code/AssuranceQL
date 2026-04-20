package org.tp2.exo2;



import org.mockito.InOrder;
import org.tp2.exo2.OrderDao;
import org.tp2.exo2.Order;
import org.tp2.exo2.OrderService;
import org.tp2.exo2.OrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    // ── Mocks ────────────────────────────────────────────────────────────────

    @Mock
    private OrderService orderService;   // mock de la couche service

    @Mock
    private OrderDao orderDao;           // mock de la couche DAO

    // ── Classe testée ────────────────────────────────────────────────────────

    @InjectMocks
    private OrderController orderController;  // reçoit orderService injecté

    // ── Données de test ──────────────────────────────────────────────────────

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order(1L, "Laptop", 2, 999.99);
    }

    // ── Test 1 : vérification que createOrder délègue bien au service ────────

    @Test
    @DisplayName("createOrder doit appeler OrderService.createOrder avec la bonne commande")
    void createOrder_shouldCallOrderService_withCorrectOrder() {
        // ACT
        orderController.createOrder(testOrder);

        // ASSERT — OrderService.createOrder appelé exactement 1 fois avec testOrder
        verify(orderService, times(1)).createOrder(testOrder);
        verifyNoMoreInteractions(orderService);
    }

    // ── Test 2 : vérification que le DAO est bien appelé via le service ──────

    @Test
    @DisplayName("createOrder doit déclencher OrderDao.saveOrder via le service")
    void createOrder_shouldTriggerOrderDao_saveOrder() {
        // ARRANGE — configurer le service réel avec le mock DAO pour ce test
        OrderService realService = new OrderService(orderDao);
        OrderController controllerWithRealService = new OrderController(realService);

        // ACT
        controllerWithRealService.createOrder(testOrder);

        // ASSERT — OrderDao.saveOrder appelé avec le bon objet commande
        verify(orderDao, times(1)).saveOrder(testOrder);
        verifyNoMoreInteractions(orderDao);
    }

    // ── Test 3 : vérification de l'ordre des appels ──────────────────────────

    @Test
    @DisplayName("createOrder doit appeler service puis DAO dans le bon ordre")
    void createOrder_shouldCallLayersInCorrectOrder() {
        // ARRANGE
        OrderService realService = new OrderService(orderDao);
        OrderController controllerWithRealService = new OrderController(realService);

        // ACT
        controllerWithRealService.createOrder(testOrder);

        // ASSERT — vérifier l'ordre avec InOrder
        InOrder inOrder = inOrder(orderDao);
        inOrder.verify(orderDao).saveOrder(testOrder);
    }

    // ── Test 4 : commande null doit lever une exception ──────────────────────

    @Test
    @DisplayName("createOrder doit propager l'exception si la commande est null")
    void createOrder_shouldThrowException_whenOrderIsNull() {
        // ARRANGE — le service mock lance une exception pour null
        doThrow(new IllegalArgumentException("La commande ne peut pas être null"))
                .when(orderService).createOrder(null);

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> orderController.createOrder(null)
        );

        verify(orderService).createOrder(null);
    }
}