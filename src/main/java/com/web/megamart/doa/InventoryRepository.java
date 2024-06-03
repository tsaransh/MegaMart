package com.web.megamart.doa;

import com.web.megamart.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InventoryRepository extends JpaRepository<Inventory, String> {



}
