package com.web.megamart.payload;

import lombok.Data;

@Data
public class AddProductInventory {

    private String productId;
    private int stockQuantity;

}
