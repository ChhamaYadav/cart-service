package org.example;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItems implements Serializable {

    private static final long serialVersionUID=1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private double price;
    private String imageUrl;
    private int quantity;

}
