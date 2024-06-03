package com.web.megamart.payload;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProductResponse {

    private String productId;
    private String productName;
    private double productAmount;
    private List<String> productImageUrl;
    private String productRegisterBy;
    private Date productRegisterDate;
    private List<ReviewResponse> productReviews;
    private GetProductInventory productInventory;
}
