package org.example;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    // Request classes
    public record CartItemRequest(Long userId, Long productId, String productName, double price, String imageUrl, int quantity) {}
    public record UpdateRequest(Long userId, Long productId, int quantity) {}

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody CartItemRequest request){
        CartItems item=new CartItems(
                request.productId(),
                request.productName(),
                request.price(),
                request.imageUrl(),
                request.quantity()
        );
        cartService.addToCart(request.userId(),item);
        System.out.println("add to cart request hit");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count/{userid}")
    public ResponseEntity<Integer> getCartCount(@PathVariable Long userId){
        return  ResponseEntity.ok(cartService.getCartCount(userId));
    }

    @GetMapping("/details/{userId}")
    public ResponseEntity<Map<String,Object>> getCartDetails(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCartDetails(userId));
    }

    @PutMapping("/update-item")
    public ResponseEntity<Void> updateItem(@RequestBody UpdateRequest request){
        cartService.updateItemQuantity(request.userId(), request.productId(), request.quantity());
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove-item")
    public ResponseEntity<Void> removeItem(@RequestBody UpdateRequest request){
       cartService.removeItem(request.userId(), request.productId());
       return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId){
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }


}
