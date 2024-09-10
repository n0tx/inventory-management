package com.example.demo.service;

import com.example.demo.dto.ItemWithStock;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Item;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    // Method to get list of item with pagination
    public Page<Item> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Page<ItemWithStock> getItemsWithStock(Pageable pageable) {
        Page<Item> items = itemRepository.findAll(pageable);

        return new PageImpl<ItemWithStock>(items.stream().map(item -> {
             Integer remainingStock = calculateRemainingStock(item);
             return new ItemWithStock(item, remainingStock);
        }).collect(Collectors.toList()));
    }

    private Integer calculateRemainingStock(Item item) {
        return inventoryRepository.calculateRemainingStock(item).orElse(0);
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    public ItemWithStock getItemByIdWithStock(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        Integer remainingStock = calculateRemainingStock(item);
        return new ItemWithStock(item, remainingStock);
    }

    public Item updateItem(Long id, Item itemDetails) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + id));

        item.setName(itemDetails.getName());
        item.setPrice(itemDetails.getPrice());

        return itemRepository.save(item);
    }
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
