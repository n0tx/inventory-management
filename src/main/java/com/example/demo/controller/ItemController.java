package com.example.demo.controller;

import com.example.demo.dto.ItemWithStock;
import com.example.demo.model.Item;
import com.example.demo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<Item> createItem(@Validated @RequestBody Item item) {
        return new ResponseEntity<>(itemService.saveItem(item), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Item>> getAllItems(@PageableDefault(size = 5) Pageable pageable) {
        return new ResponseEntity<>(itemService.getAllItems(pageable), HttpStatus.OK);
    }

    @GetMapping("/with-stock")
    public ResponseEntity<Page<ItemWithStock>> getAllItemsWithStock(@PageableDefault(size = 5) Pageable pageable) {
        return new ResponseEntity<>(itemService.getItemsWithStock(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.getItemById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/with-stock")
    public ResponseEntity<ItemWithStock> getItemByIdWithStock(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.getItemByIdWithStock(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @Validated @RequestBody Item itemDetails) {
        return new ResponseEntity<>(itemService.updateItem(id, itemDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
