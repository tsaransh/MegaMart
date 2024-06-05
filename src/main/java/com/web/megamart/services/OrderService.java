package com.web.megamart.services;

import com.web.megamart.entity.helper.OrderStatus;
import com.web.megamart.payload.OrderDetails;
import com.web.megamart.payload.PlaceOrder;

import java.util.List;

public interface OrderService {

    public OrderDetails placeOrder(PlaceOrder placeOrder);
    public boolean cancelOrder(String orderId);

    public List<OrderDetails> getAllOrder(String userUid); // Only for admins
    public List<OrderDetails> getAllOrderByOrderTo(String userUid);
    public List<OrderDetails> getAllOrderByOrderTo(String userUid, String value);
    public List<OrderDetails> getAllOrderByOrderBy(String userUid);
    public OrderDetails orderCompleted(String orderId);

    public OrderDetails changeOrderStatus(String orderId, String status);
    public String getOrderStatus(String orderId);

}
