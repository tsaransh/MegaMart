package com.web.megamart.payload;

import lombok.Data;

@Data
public class PlaceOrder {

    private int quantity;
    private String productId;
    private String oderBy;
    private String deliveryAddress;

}
