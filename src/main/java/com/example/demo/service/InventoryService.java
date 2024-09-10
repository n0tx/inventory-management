package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Inventory;
import com.example.demo.model.Item;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    public Inventory saveInventory(Inventory inventory) {

        Item item = itemRepository.findById(inventory.getItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        int stockInventoryItem = inventoryRepository.calculateRemainingStock(item).orElse(0);

        // Check transaction type: 'T' for Top-Up, 'W' for Withdrawal
        if (inventory.getType().equals("T")) {
            inventory.setQty(inventory.getQty() + stockInventoryItem);
        } else if (inventory.getType().equals("W")) {
            inventory.setQty(stockInventoryItem - inventory.getQty());
        }
        return inventoryRepository.save(inventory);
    }

    public Page<Inventory> getAllInventories(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
    }

    public Inventory updateInventory(Long id, Inventory inventoryDetails) {

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for this id :: " + id));

        inventory.setItem(inventoryDetails.getItem());
        inventory.setQty(inventoryDetails.getQty());
        inventory.setType(inventoryDetails.getType());

        return inventoryRepository.save(inventory);
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

}