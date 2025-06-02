package org.example;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    private GuestCartService guestCartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    // Request classes
    public record CartItemRequest(Long userId, Long productId, String productName, double price, String imageUrl, int quantity) {}
    public record UpdateRequest(Long userId, Long productId, int quantity) {}

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody CartItemRequest request, HttpServletRequest httpRequest){
        CartItems item=new CartItems(
                request.productId(),
                request.productName(),
                request.price(),
                request.imageUrl(),
                request.quantity()
        );
        if(request.userId()!=null){
            //Logged-in user
            cartService.addToCart(request.userId(),item);
            System.out.println("Item added to logged in user's cart");
        }else{
            //Guest user use session
            HttpSession session=httpRequest.getSession(true);//create if not exists
            String sessionId=session.getId();
            guestCartService.addToGuestCart(sessionId,item);
            System.out.println("Item added to guest user's cart with sessionId = " + sessionId);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<Integer> getCartCount(@PathVariable Long userId){
        System.out.println("getCartCount API hit");
        return  ResponseEntity.ok(cartService.getCartCount(userId));
    }

    //Details fo the products visible
    @GetMapping("/details/{userId}")
    public ResponseEntity<Map<String,Object>> getCartDetails(@PathVariable Long userId){
        System.out.println("Details API endpoints hit");
        return ResponseEntity.ok(cartService.getCartDetails(userId));
    }

    @PutMapping("/update-item")
    public ResponseEntity<Void> updateItem(@RequestBody UpdateRequest request){
        cartService.updateItemQuantity(request.userId(), request.productId(), request.quantity());
        System.out.println("update endpoint called");
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove-item")
    public ResponseEntity<Void> removeItem(@RequestBody UpdateRequest request){
       cartService.removeItem(request.userId(), request.productId());
       System.out.println("Delete API endpoint called");
       return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId){
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }


}
