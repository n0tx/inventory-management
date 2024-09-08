package com.example.demo.dto;

import com.example.demo.model.Item;
import lombok.Data;

@Data
public class ItemWithStock {
    private Long id;
    private String name;
    private Integer price;
    private Integer remainingStock;

    public ItemWithStock(Item item, Integer remainingStock) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.remainingStock = remainingStock != null ? remainingStock : 0;
    }
}
