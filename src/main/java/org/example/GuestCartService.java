package org.example;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class GuestCartService {

private final RedisTemplate<String,Object> redisTemplate;
private static final String GUEST_CART_PREFIX="guest_cart";

    public GuestCartService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void addToGuestCart(String sessionId, CartItems item) {
        String key=GUEST_CART_PREFIX+sessionId;
        List<CartItems> cart=(List<CartItems>) redisTemplate.opsForValue().get(key);

        if(cart==null){
            cart=new ArrayList<>();
        }
        cart.add(item);
        redisTemplate.opsForValue().set(key,cart, Duration.ofHours(4)); // expire after 4 hours

    }
}
