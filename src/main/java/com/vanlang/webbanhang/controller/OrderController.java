package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.CartItem;
import com.vanlang.webbanhang.model.Order;
import com.vanlang.webbanhang.model.Product; // Import Product model
import com.vanlang.webbanhang.service.CartService;
import com.vanlang.webbanhang.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping("/checkout")
    public String checkout() {
        return "cart/checkout";
    }

    @PostMapping("/submit")
    public String submitOrder(@RequestParam String customerName, @RequestParam String address, @RequestParam String phone,
                              @RequestParam String email, @RequestParam String note, @RequestParam String paymentMethod,
                              @RequestParam String deliveryDate) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }

        // Parse the deliveryDate to LocalDate
        LocalDate parsedDeliveryDate = LocalDate.parse(deliveryDate);

        // Create order
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setAddress(address);
        order.setPhone(phone);
        order.setEmail(email);
        order.setNote(note);
        order.setPaymentMethod(paymentMethod);
        order.setDeliveryDate(parsedDeliveryDate);

        // Save order and order details
        orderService.createOrder(order, cartItems);

        return "redirect:/order/confirmation";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        // Example delivery date and fee calculation based on your logic
        LocalDate orderDate = LocalDate.now();
        LocalDate deliveryDate;
        int deliveryFee = 0;
        String deliveryMessage;

        if (orderDate.getDayOfWeek().getValue() == 5) { // Friday
            deliveryDate = orderDate.plusDays(5);
            deliveryFee = 5;
            deliveryMessage = "Delivery in 5 days ($5 additional fee)";
        } else if (orderDate.getDayOfWeek().getValue() == 3) { // Wednesday
            deliveryDate = orderDate.plusDays(3);
            deliveryFee = 10;
            deliveryMessage = "Delivery in 3 days ($10 additional fee)";
        } else { // Default case
            deliveryDate = orderDate.plusDays(7);
            deliveryMessage = "Delivery in 7 days (No additional fee)";
        }

        // Set attributes in model
        model.addAttribute("message", "Your order has been successfully placed.");
        model.addAttribute("deliveryDate", deliveryDate);
        model.addAttribute("deliveryFee", deliveryFee);
        model.addAttribute("deliveryMessage", deliveryMessage);

        return "cart/order-confirmation";
    }

    @GetMapping("/management")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String orderManagement(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);

        double totalRevenue = orderService.calculateTotalRevenue();
        model.addAttribute("totalRevenue", totalRevenue);

        return "order/order-management";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrderById(id);
        return "redirect:/order/management";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editOrder(@PathVariable("id") Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order/edit-order";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateOrder(@ModelAttribute("order") Order order) {
        orderService.updateOrder(order);
        return "redirect:/order/management";
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String orderStatistics(Model model) {
        int totalOrders = orderService.getAllOrders().size();
        double totalRevenue = orderService.calculateTotalRevenue();
        Product bestSellingProduct = orderService.findBestSellingProduct();

        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("bestSellingProduct", bestSellingProduct);

        return "order/order-statistics";
    }

    @ModelAttribute("order")
    public Order orderModelAttribute() {
        return new Order();
    }
}
