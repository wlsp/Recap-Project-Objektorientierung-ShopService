import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {
    private final IdService idService = new IdService();

    @Test
    void addOrderTest() throws NoProductFoundException {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, Instant.now());
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());

        Timestamp timestamp = null;
        System.out.println(timestamp);
    }

    @Test
    void getOrdersByStatus_returnsListWithCompletedOrders() throws NoProductFoundException {
        // GIVEN
        ShopService shopService = new ShopService();
        Product product = new Product("1", "Apfel");
        OrderStatus expectedStatus = OrderStatus.PROCESSING;

        Order order1 = new Order("1", List.of(product), expectedStatus, Instant.now());
        shopService.addOrder(List.of("1"));

        // WHEN
        List<Order> result = shopService.getOrdersByStatus(expectedStatus);
        System.out.println("result: " + result);
        // THEN
        assertNotNull(result, "The returned list should never be null.");
        assertEquals(List.of(order1.status()), result.stream().map(Order::status).toList(),
                "The list should contain only the order with status COMPLETED.");
    }

    @Test
    void getOrdersByStatus_returnsEmptyListWhenNoCompletedOrdersExist() throws NoProductFoundException {
        // GIVEN
        ShopService shopService = new ShopService();

        shopService.addOrder(List.of("1"));
        // WHEN
        List<Order> result = shopService.getOrdersByStatus(OrderStatus.COMPLETED);

        // THEN
        assertNotNull(result, "The returned list should never be null.");
        assertTrue(result.isEmpty(),
                "When no orders have the status COMPLETED, an empty list should be returned.");
    }

}
