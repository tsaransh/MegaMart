package com.web.megamart.payload;

import lombok.Data;

import java.util.Date;

@Data
public class GetProductInventory {

    private int stockQuantity;
    private Date createDate;
    private Date lastUpdate;

}
