package com.web.megamart.payload;

import lombok.Data;


import java.util.Date;
import java.util.Map;
import java.util.List;

@Data
public class OrderDetails {

    private String orderId;
    private int quantity;
    private ProductResponse productResponse;
    private AuthenticatedUserDetails userDetails;
    private Date orderDate;
    private Date deliveryDate;
    private String deliveryAddress;
    private List<Map<String, Date>> orderStatus;
    private boolean completed;

}
