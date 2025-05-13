package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void addToCart(Long userId,CartItems cartItems){
        Cart cart= cartRepository.findById(userId).orElse(new Cart(userId,new ArrayList<>()));

        Optional<CartItems> existing=cart.getItem().stream()
                .filter(i->i.getProductId().equals(cartItems.getProductId()))
                .findFirst();
        if(existing.isPresent()){
            existing.get().setQuantity(existing.get().getQuantity()+cartItems.getQuantity());
        }
        else{
            cart.getItem().add(cartItems);
        }
        cartRepository.save(cart);
    }

    public int getCartCount(Long userId){
        return cartRepository.findById(userId)
                .map(c->c.getItem().stream().mapToInt(CartItems::getQuantity).sum())
                .orElse(0);
    }


}
