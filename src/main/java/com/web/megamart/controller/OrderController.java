package com.web.megamart.controller;

import com.web.megamart.exception.InventoryException;
import com.web.megamart.exception.OrderException;
import com.web.megamart.exception.ProductException;
import com.web.megamart.exception.UserException;
import com.web.megamart.payload.OrderDetails;
import com.web.megamart.payload.PlaceOrder;
import com.web.megamart.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/cancel")
    public ResponseEntity<Boolean> cancelOrder(@RequestParam String orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @GetMapping("/get-order-status")
    public ResponseEntity<String> getOrderStatus(@RequestParam String orderId) {
        return ResponseEntity.ok(orderService.getOrderStatus(orderId));
    }

    @PostMapping("/change-order-status")
    public ResponseEntity<?> changeOrderStatus(@RequestParam String orderId, String orderStatus) {
        return ResponseEntity.ok(orderService.changeOrderStatus(orderId, orderStatus));
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<OrderDetails>> getAllOrders(@RequestParam String userUid) {
        return ResponseEntity.ok(orderService.getAllOrder(userUid));
    }

    @GetMapping("/get-orders-for-seller")
    public ResponseEntity<List<OrderDetails>> getAllOrdersForSeller(@RequestParam String userUid) {
        return ResponseEntity.ok(orderService.getAllOrderByOrderTo(userUid));
    }

    @GetMapping("/get-orders-for-seller-by-filter")
    public ResponseEntity<List<OrderDetails>> getAllOrdersForSellerByFilter(@RequestParam String userUid, String value) {
        return ResponseEntity.ok(orderService.getAllOrderByOrderTo(userUid, value));
    }

    @PostMapping("/mark-as-complete")
    public ResponseEntity<?> orderCompleted(@RequestParam String orderId) {
        return ResponseEntity.ok(orderService.orderCompleted(orderId));
    }

    @GetMapping("/get-orders-for-buyer")
    public ResponseEntity<List<OrderDetails>> getAllOrdersForBuyer(@RequestParam String userUid) {
        return ResponseEntity.ok(orderService.getAllOrderByOrderBy(userUid));
    }


    @ExceptionHandler(OrderException.class)
    public ResponseEntity<String> orderException(OrderException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.getMessage());
    }
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<String> handleProductException(ProductException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.getMessage());
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }
    @ExceptionHandler(InventoryException.class)
    public ResponseEntity<String> handleInventoryException(InventoryException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }

}
