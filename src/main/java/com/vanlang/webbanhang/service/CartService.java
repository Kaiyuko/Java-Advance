package com.vanlang.webbanhang.service;

import com.vanlang.webbanhang.model.CartItem;
import com.vanlang.webbanhang.model.Order;
import com.vanlang.webbanhang.model.OrderDetail;
import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.repository.OrderRepository;
import com.vanlang.webbanhang.repository.OrderDetailRepository;
import com.vanlang.webbanhang.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class CartService {
    private List<CartItem> cartItems = new ArrayList<>();

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public void addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        cartItems.add(new CartItem(product, quantity));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void removeFromCart(Long productId) {
        cartItems.removeIf(cartItem -> cartItem.getProduct().getId().equals(productId));
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void checkout(Order order) {
        // Lưu thông tin đơn hàng
        orderRepository.save(order);

        // Lưu chi tiết đơn hàng
        for (CartItem cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetailRepository.save(orderDetail);
        }

        // Xóa giỏ hàng sau khi đặt hàng thành công
        clearCart();
    }

    public void clearCard() {
        cartItems.clear();
    }
}