import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShopService {
    private final ProductRepo productRepo = new ProductRepo();
    private final OrderRepo orderRepo = new OrderMapRepo();
    private final IdService idService = new IdService();

    public Order addOrder(List<String> productIds) throws NoProductFoundException {
        List<Product> products = new ArrayList<>();

        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                throw new NoProductFoundException("No product with the id: " + productId + " found. Try again!");

            }
            products.add(productToOrder.get());
        }

        Order newOrder = new Order(idService.generateOrderId(), products, OrderStatus.PROCESSING, Instant.now());

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepo.getOrders().stream()
                .filter(order -> order.status() == status)
                .toList();
    }


    public OrderStatus uptadeOrderStatus(String id, OrderStatus status) {
        Order order = orderRepo.getOrderById(id).withStatus(status);
        orderRepo.removeOrder(id);
        orderRepo.addOrder(order);
        return order.status();
    }
}
