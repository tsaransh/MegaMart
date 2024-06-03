package com.web.megamart.payload;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class AddProduct {

    private String productName;
    private double productAmount;
    private List<String> productImageUrl;
    private String productRegisterBy;
    private int quantity;

}
