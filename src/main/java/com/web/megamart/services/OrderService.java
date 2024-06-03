package com.web.megamart.services;

import com.web.megamart.payload.OrderDetails;
import com.web.megamart.payload.PlaceOrder;

import java.util.List;

public interface OrderService {

    public OrderDetails placeOrder(PlaceOrder placeOrder);
    public OrderDetails cancelOrder(String orderId);

    public List<OrderDetails> getAllOrder(String userUid); // Only for admins
    public List<OrderDetails> getAllOrderByUserId(String userUid);

    public OrderDetails orderCompleted(String orderId);

    public OrderDetails changeOrderStatus(String oderId, String status);
    public String getOrderStatus(String orderId);

}
