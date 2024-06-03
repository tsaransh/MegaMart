package com.web.megamart.controller;

import com.web.megamart.payload.AddProductInventory;
import com.web.megamart.payload.GetProductInventory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    public ResponseEntity<GetProductInventory> addItem(AddProductInventory productInventory) {
        return null;
    }

}
