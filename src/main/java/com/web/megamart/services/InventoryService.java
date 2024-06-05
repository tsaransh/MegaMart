package com.web.megamart.services;

import com.web.megamart.entity.Inventory;

import java.util.List;

public interface InventoryService {

    public Inventory addItem(Inventory inventory);
    public boolean deleteItem(String productId);
    public int fetchQuantity(String productId);
    public Inventory update(Inventory inventory);

    public List<Inventory> getAll();

}
