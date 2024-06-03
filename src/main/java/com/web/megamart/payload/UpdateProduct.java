package com.web.megamart.payload;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProduct {

    private String productId;
    private String productName;
    private double productAmount;
    private List<String> productImageUrl;
    private String productRegisterBy;
    private int quantity;

}
