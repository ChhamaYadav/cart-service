package org.example;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItems {

    private Long productId;
    private String productName;
    private double price;
    private String imageUrl;
    private int quantity;

}
