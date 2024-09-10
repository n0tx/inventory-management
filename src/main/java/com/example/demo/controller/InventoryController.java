package com.example.demo.controller;

import com.example.demo.model.Inventory;
import com.example.demo.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@Validated @RequestBody Inventory inventory) {
        return new ResponseEntity<>(inventoryService.saveInventory(inventory), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Inventory>> getAllInventories(@PageableDefault(size = 5) Pageable pageable) {
        return new ResponseEntity<>(inventoryService.getAllInventories(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        return new ResponseEntity<>(inventoryService.getInventoryById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @Validated @RequestBody Inventory inventoryDetails) {
        return new ResponseEntity<>(inventoryService.updateInventory(id, inventoryDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
