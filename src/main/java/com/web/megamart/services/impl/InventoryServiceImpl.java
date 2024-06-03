package com.web.megamart.services.impl;

import com.web.megamart.doa.InventoryRepository;
import com.web.megamart.entity.Inventory;
import com.web.megamart.entity.Product;
import com.web.megamart.exception.InventoryException;
import com.web.megamart.payload.GetProductInventory;
import com.web.megamart.services.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;


    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Inventory addItem(Inventory inventory) {
        System.out.println("Inside Inventory Services -> "+inventory.toString());
        return inventoryRepository.save(inventory);
    }

    @Override
    public boolean deleteItem(String inventoryId) {
        Inventory inventory = fetchInventory(inventoryId);
        inventoryRepository.delete(inventory);
        return true;
    }

    private Inventory fetchInventory(String inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryException("Not Inventory found with product id:"+inventoryId,
                        HttpStatus.NOT_FOUND));
    }

    @Override
    public int fetchQuantity(String inventoryId) {
        Inventory inventory = fetchInventory(inventoryId);
        return inventory.getStockQuantity();
    }

    @Override
    public Inventory update(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public List<Inventory> getAll() {
        List<Inventory> inventoryDetailList = inventoryRepository.findAll();

        return inventoryDetailList;
    }
}
