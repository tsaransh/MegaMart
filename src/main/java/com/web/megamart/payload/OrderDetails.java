package com.web.megamart.payload;

import com.web.megamart.entity.helper.OrderStatus;
import lombok.Data;


import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@Data
public class OrderDetails {

    private String orderId;
    private int quantity;
    private ProductResponse productResponse;
    private AuthenticatedUserDetails userDetails;
    private Date orderDate;
    private LocalDate deliveryDate;
    private String deliveryAddress;
    private Map<OrderStatus, LocalDate> orderStatus;
    private boolean completed;
    private String orderTo;

}
