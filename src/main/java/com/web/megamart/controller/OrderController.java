package com.web.megamart.controller;

import com.web.megamart.exception.OrderException;
import com.web.megamart.payload.OrderDetails;
import com.web.megamart.payload.PlaceOrder;
import com.web.megamart.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDetails> placeOrder(@RequestBody PlaceOrder placeOrder) {
        return ResponseEntity.ok(orderService.placeOrder(placeOrder));
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<String> orderException(OrderException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.getMessage());
}

}
