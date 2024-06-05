package com.web.megamart.services.impl;

import com.web.megamart.doa.OrderRepository;
import com.web.megamart.entity.*;
import com.web.megamart.entity.helper.AccountType;
import com.web.megamart.entity.helper.OrderStatus;
import com.web.megamart.exception.OrderException;
import com.web.megamart.exception.UserException;
import com.web.megamart.payload.*;
import com.web.megamart.services.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServicesImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final UserService userService;
    private final InventoryService inventoryService;
    private final MailService mailService;

    @Autowired
    public OrderServicesImpl(OrderRepository orderRepository, ModelMapper modelMapper, ProductService productService, UserService userService, InventoryService inventoryService, MailService mailService) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.userService = userService;
        this.inventoryService = inventoryService;
        this.mailService = mailService;
    }

    @Override
    public synchronized OrderDetails placeOrder(PlaceOrder placeOrder) {
        Order order = modelMapper.map(placeOrder, Order.class);
        Product product = productService.getProductById(placeOrder.getProductId());

        order.setOrderTo(product.getProductRegisterBy());
        UserDetail sellerDetails = userService.findByUserUid(product.getProductRegisterBy());

        UserDetail userDetail = userService.findByUserUid(placeOrder.getOderBy());
        Inventory inventory = product.getInventory();

        if(placeOrder.getQuantity() <= inventory.getStockQuantity()) {
            if(userDetail.getUserAddress()!=null) {
                userDetail.setUserAddress(placeOrder.getDeliveryAddress());
                userService.save(userDetail);
            }
            order.setProduct(product);
            order.setDeliveryDate(LocalDate.now().plusDays(7));
            order.setOrderStatus((Map<OrderStatus, Date>) new HashMap<>().put(OrderStatus.ORDER_RECEIVED, LocalDate.now()));
            Order savedOrder = orderRepository.save(order);

            int newQuantity = inventory.getStockQuantity() - placeOrder.getQuantity();
            inventory.setStockQuantity(newQuantity);
            inventoryService.update(inventory);

            OrderDetails orderDetails =  mappedToOrderDetail(savedOrder);
            sendMailToBuyer(orderDetails);
            sendMailToSeller(orderDetails, sellerDetails.getEmail());

            return orderDetails;
        } else {
            throw new OrderException("Only "+inventory.getStockQuantity()+" item are available", HttpStatus.BAD_REQUEST);
        }
    }

    private void sendMailToSeller(OrderDetails orderDetails, String email) {
        MailData mailData = new MailData();

        mailData.setSendTo(email);
        mailData.setSubject("New Order Received");
        String message = "Congratulations, You received a new order of " +
                "<b>"+orderDetails.getProductResponse().getProductName()+"</b> "+
                "with quantity of <b>"+orderDetails.getQuantity()+"</b>"+
                "<br><br><br>"+
                "order will be delivered by <u>"+orderDetails.getDeliveryDate()+"</u> "+
                "the delivery address is <b>"+orderDetails.getDeliveryAddress()+"</b> "+
                "<br><br><br><br><br><br>"+
                "Team<br>"+
                "Mega Mart";

        mailData.setMessage(message);

        mailService.sendMail(mailData);
    }

    private void sendMailToBuyer(OrderDetails orderDetails) {

        MailData mailData = new MailData();
        mailData.setSendTo(orderDetails.getUserDetails().getEmail());
        mailData.setSubject("Order Confirmation");
        String message = "Congratulations, Your order of " +
                "<b>"+orderDetails.getProductResponse().getProductName()+"</b> "+
                "is successfully placed."+
                "<br><br><br>"+
                "Your order will be delivered by <u>"+orderDetails.getDeliveryDate()+"</u> "+
                "Your delivery address is <b>"+orderDetails.getDeliveryAddress()+"</b>"+
                "<br><br><br><br><br><br>"+
                "Team<br>"+
                "Mega Mart";

        mailData.setMessage(message);

        mailService.sendMail(mailData);

    }

    private OrderDetails mappedToOrderDetail(Order order) {
        OrderDetails orderDetails = new OrderDetails();

        orderDetails.setOrderId(order.getOrderId());
        orderDetails.setQuantity(order.getQuantity());
        orderDetails.setProductResponse(mappedToProductResponse(order.getProduct()));

        UserDetail userDetail = userService.findByUserUid(order.getOrderBy());
        orderDetails.setUserDetails(userService.fetchUser(userDetail.getEmail()));

        orderDetails.setOrderDate(order.getOrderDate());
        orderDetails.setDeliveryDate(order.getDeliveryDate());
        orderDetails.setDeliveryAddress(order.getDeliveryAddress());
        orderDetails.setOrderStatus((Map<OrderStatus, LocalDate>) new HashMap<>().put(OrderStatus.ORDER_RECEIVED.toString(), LocalDate.now()));
        orderDetails.setCompleted(false);
        orderDetails.setOrderTo(order.getOrderTo());
        return orderDetails;
    }

    private ProductResponse mappedToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();

        // Mapped to Product Response
        response.setProductId(product.getProductId());
        response.setProductName(product.getProductName());
        response.setProductAmount(product.getProductAmount());
        response.setProductImageUrl(product.getProductImageUrl());
        response.setProductRegisterBy(product.getProductRegisterBy());
        response.setProductRegisterDate(product.getProductRegisterDate());

        if(product.getProductReviews()!=null) {
            List<ProductReview> productReview = product.getProductReviews();
            List<ReviewResponse> reviewResponse = productReview.stream()
                    .map(p -> modelMapper.map(p, ReviewResponse.class))
                    .collect(Collectors.toList());
            response.setProductReviews(reviewResponse);
        }

        GetProductInventory productInventory = new GetProductInventory();
        Inventory inventory = product.getInventory();
        productInventory.setStockQuantity(inventory.getStockQuantity());
        productInventory.setCreateDate(inventory.getCreateDate());
        productInventory.setLastUpdate(inventory.getUpdateDate());
        response.setProductInventory(productInventory);

        return response;
    }

    @Override
    public synchronized boolean cancelOrder(String orderId) {
        Order order = getOrder(orderId);
        int orderQuantity = order.getQuantity();

        Inventory inventory = order.getProduct().getInventory();
        int newQuantity = inventory.getStockQuantity() +  orderQuantity;
        inventory.setStockQuantity(newQuantity);
        inventoryService.update(inventory);

        order.setOrderStatus((Map<OrderStatus, Date>) new HashMap<>().put(OrderStatus.CANCELLED, LocalDate.now()));
        orderRepository.save(order);
        return true;
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
    public List<OrderDetails> getAllOrderByOrderTo(String userUid) {
        List<Order> orders = orderRepository.findByOrderTo(userUid)
                .orElseThrow(() -> new OrderException("No order found at that moment!", HttpStatus.NOT_FOUND));

        return orders.stream().map((order) -> mappedToOrderDetail(order))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDetails> getAllOrderByOrderTo(String userUid, String value) {
        value = value.toUpperCase();
        List<OrderDetails> orderDetails = getAllOrderByOrderTo(userUid);

        if (value.compareTo(OrderStatus.DELIVERED.toString()) == 0) {
            return orderDetails.stream().filter((order) -> order.isCompleted())
                    .collect(Collectors.toList());
        }
        else if(value.compareTo(OrderStatus.CANCELLED.toString())==0) {
            return orderDetails.stream().filter(
                    (order) -> order.getOrderStatus().toString().
                            compareTo(OrderStatus.CANCELLED.toString())==0)
                    .collect(Collectors.toList());
        }
        else {
            return orderDetails.stream().filter((order) -> !order.isCompleted())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<OrderDetails> getAllOrderByOrderBy(String userUid) {
        List<Order> orders = orderRepository.findByOrderBy(userUid)
                .orElseThrow(() -> new OrderException("No order found, Please order something!", HttpStatus.NOT_FOUND));
        return orders.stream().map((order) -> mappedToOrderDetail(order))
                .collect(Collectors.toList());
    }


    @Override
    public OrderDetails orderCompleted(String orderId) {
        Order order = getOrder(orderId);
        UserDetail userDetail = userService.findByUserUid(order.getOrderTo());
        if(userDetail.getAccountType()== AccountType.SELLER.toString()) {
            throw new UserException("You don't have any right to change order status", HttpStatus.BAD_REQUEST);
        }
        changeOrderStatus(orderId, OrderStatus.DELIVERED.toString());
        order.setCompleted(true);
        return mappedToOrderDetail(orderRepository.save(order));
    }

    @Override
    public OrderDetails changeOrderStatus(String orderId, String status) {
        Order order = getOrder(orderId);
        UserDetail userDetail = userService.findByUserUid(order.getOrderTo());
        if(userDetail.getAccountType()== AccountType.SELLER.toString()) {
            throw new UserException("You don't have any right to change order status", HttpStatus.BAD_REQUEST);
        }
        order.setOrderStatus((Map<OrderStatus, Date>) new HashMap<>().put(status, LocalDate.now()));
        return mappedToOrderDetail(orderRepository.save(order));
    }

    private Order getOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Can not found any Order", HttpStatus.BAD_REQUEST));
    }

    @Override
    public String getOrderStatus(String orderId) {
        return getOrder(orderId).getOrderStatus().toString();
    }
}
