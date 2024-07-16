package com.vanlang.webbanhang.service;

import com.vanlang.webbanhang.model.CartItem;
import com.vanlang.webbanhang.model.Order;
import com.vanlang.webbanhang.model.OrderDetail;
import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.repository.OrderDetailRepository;
import com.vanlang.webbanhang.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Transactional
    public void createOrder(Order order, List<CartItem> cartItems) {
        orderRepository.save(order); // Save order first to generate order ID

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            if (cartItem != null && cartItem.getProduct() != null) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(cartItem.getProduct());
                orderDetail.setQuantity(cartItem.getQuantity());

                // Ensure getPrice() returns double
                double price = cartItem.getProduct().getPrice();
                orderDetail.setPrice(price);

                orderDetails.add(orderDetail);
            } else {
                throw new IllegalArgumentException("CartItem or Product cannot be null.");
            }
        }

        orderDetailRepository.saveAll(orderDetails);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateOrder(Order order) {
        orderRepository.save(order);
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    public double calculateTotalRevenue() {
        List<Order> orders = orderRepository.findAll();
        double totalRevenue = 0;

        for (Order order : orders) {
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
            for (OrderDetail orderDetail : orderDetails) {
                totalRevenue += orderDetail.getQuantity() * orderDetail.getPrice();
            }
        }

        return totalRevenue;
    }

    public Product findBestSellingProduct() {
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();

        // Group by product and sum quantities
        Map<Product, Integer> productQuantities = orderDetails.stream()
                .collect(Collectors.groupingBy(OrderDetail::getProduct,
                        Collectors.summingInt(OrderDetail::getQuantity)));

        // Find product with maximum quantity sold
        Product bestProduct = null;
        int maxQuantity = 0;
        for (Map.Entry<Product, Integer> entry : productQuantities.entrySet()) {
            if (entry.getValue() > maxQuantity) {
                maxQuantity = entry.getValue();
                bestProduct = entry.getKey();
            }
        }

        return bestProduct;
    }
}
