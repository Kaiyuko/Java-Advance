package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.Order;
import com.vanlang.webbanhang.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("order", new Order());
        return "/cart/cart";
    }
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.addToCart(productId, quantity);
        return "redirect:/cart";
    }
    @PostMapping("/checkout")
    public String checkout(@ModelAttribute Order order) {
        cartService.checkout(order);
        return "redirect:/cart/confirmation";
    }
    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }
    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCard();
        return "redirect:/cart";
    }
}
