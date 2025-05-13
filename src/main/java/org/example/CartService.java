package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    @Autowired
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void addToCart(Long userId, CartItems cartItems) {
        Cart cart = cartRepository.findById(userId).orElse(new Cart(userId, new ArrayList<>()));

        Optional<CartItems> existing = cart.getItem().stream()
                .filter(i -> i.getProductId().equals(cartItems.getProductId()))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + cartItems.getQuantity());
        } else {
            cart.getItem().add(cartItems);
        }
        cartRepository.save(cart);
    }

    public int getCartCount(Long userId) {
        return cartRepository.findById(userId)
                .map(c -> c.getItem().stream().mapToInt(CartItems::getQuantity).sum())
                .orElse(0);
    }

    public Map<String, Object> getCartDetails(Long userId) {
        Cart cart = cartRepository.findById(userId).orElse(null);
        if (cart == null) return Map.of("items", List.of(), "totalAmount", 0);
        List<Map<String, Object>> items = new ArrayList<>();
        double total = 0;

        for (CartItems i : cart.getItem()) {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", i.getProductId());
            map.put("productName", i.getProductName());
            map.put("price", i.getPrice());
            map.put("quantity", i.getQuantity());
            map.put("imageURL", i.getImageUrl());
            double subtotal = i.getPrice() * i.getQuantity();
            map.put("total", subtotal);
            total += subtotal;
            items.add(map);

        }
        return Map.of("items", items, "totalAmount", total);
    }

    public void updateItemQuantity(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(userId).orElse(null);
        if (cart == null) return;
        cart.getItem().removeIf(item -> item.getProductId().equals(productId));
        cartRepository.save(cart);
    }

    public void removeItem(Long userId,Long productId){
        Cart cart=cartRepository.findById(userId).orElse(null);
        if(cart==null) return;
        cart.getItem().removeIf(item->item.getProductId().equals(productId));
        cartRepository.save(cart);
    }

    public void clearCart(Long userId){
        cartRepository.deleteById(userId);
    }


}
