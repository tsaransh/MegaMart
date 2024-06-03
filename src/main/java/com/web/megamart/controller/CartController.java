package com.web.megamart.controller;

import com.web.megamart.payload.AddToCart;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/cart")
public class CartController {

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCart addToCart) {
        return null;
    }



}
