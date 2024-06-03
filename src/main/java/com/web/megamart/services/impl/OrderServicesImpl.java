package com.web.megamart.services.impl;

import com.web.megamart.doa.OrderRepository;
import com.web.megamart.entity.Order;
import com.web.megamart.entity.Product;
import com.web.megamart.entity.UserDetail;
import com.web.megamart.exception.OrderException;
import com.web.megamart.payload.OrderDetails;
import com.web.megamart.payload.PlaceOrder;
import com.web.megamart.services.OrderService;
import com.web.megamart.services.ProductService;
import com.web.megamart.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServicesImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public OrderServicesImpl(OrderRepository orderRepository, ModelMapper modelMapper, ProductService productService, UserService userService) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public OrderDetails placeOrder(PlaceOrder placeOrder) {
        Order order = modelMapper.map(placeOrder, Order.class);
        Product product = productService.getProductById(placeOrder.getProductId());
        order.setProduct(product);
        order.setDeliveryDate(LocalDate.now().plusDays(7));

        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDetails.class);
    }

    @Override
    public OrderDetails cancelOrder(String orderId) {
        return null;
    }

    @Override
    public List<OrderDetails> getAllOrder(String userUid) {
        UserDetail userDetail = userService.findByUserUid(userUid);
        if (userDetail.getRole().compareTo("ADMIN") == 0) {
            List<Order> orderList = orderRepository.findAll();
            return orderList.stream()
                    .map((order) -> modelMapper.map(order, OrderDetails.class))
                    .collect(Collectors.toList());
        }
        throw new OrderException("You don't have a right to see all order", HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<OrderDetails> getAllOrderByUserId(String userUid) {
        return List.of();
    }

    @Override
    public OrderDetails orderCompleted(String orderId) {
        return null;
    }

    @Override
    public OrderDetails changeOrderStatus(String oderId, String status) {
        return null;
    }

    @Override
    public String getOrderStatus(String orderId) {
        return "";
    }
}
