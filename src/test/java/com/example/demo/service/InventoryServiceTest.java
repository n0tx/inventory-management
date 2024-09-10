package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Inventory;
import com.example.demo.model.Item;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private InventoryService inventoryService;

    public InventoryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveInventory_TopUp() {
        // existing stock item quantity
        Item item = new Item(1L, "Pen", 5);
        Inventory stockInventory = new Inventory(1L, item, 5, "T");

        // Top Up stock item quantity
        Inventory topUpInventory = new Inventory(2L, item, 2, "T");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(inventoryRepository.calculateRemainingStock(item)).thenReturn(Optional.of(stockInventory.getQty()));
        when(inventoryRepository.save(topUpInventory)).thenReturn(topUpInventory);

        Inventory savedInventory = inventoryService.saveInventory(topUpInventory);

        assertNotNull(savedInventory);
        assertEquals(2L, savedInventory.getId());
        assertEquals("Pen", savedInventory.getItem().getName());
        assertEquals(7, savedInventory.getQty()); // topUpInventory + stockInventory
        assertEquals("T", savedInventory.getType());
    }

    @Test
    void testSaveInventory_Withdrawal() {
        // existing stock item quantity
        Item item = new Item(1L, "Pen", 5);
        Inventory stockInventory = new Inventory(1L, item, 5, "T");

        // Withdrawal stock item quantity
        Inventory withdrawalInventory = new Inventory(2L, item, 2, "W");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(inventoryRepository.calculateRemainingStock(item)).thenReturn(Optional.of(stockInventory.getQty()));
        when(inventoryRepository.save(withdrawalInventory)).thenReturn(withdrawalInventory);

        Inventory savedInventory = inventoryService.saveInventory(withdrawalInventory);

        assertNotNull(savedInventory);
        assertEquals(2L, savedInventory.getId());
        assertEquals("Pen", savedInventory.getItem().getName());
        assertEquals(3, savedInventory.getQty()); // stockInventory - withdrawalInventory
        assertEquals("W", savedInventory.getType());
    }

    @Test
    void testGetAllInventories() {
        List<Inventory> inventories = new ArrayList<Inventory>();

        inventories.add(new Inventory(1L, new Item(1L, "Pen", 5), 5, "T"));
        inventories.add(new Inventory(2L, new Item(2L, "Book", 10), 10, "T"));
        inventories.add(new Inventory(3L, new Item(3L, "Bag", 30), 30, "T"));

        when(inventoryRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(inventories));

        Page<Inventory> foundInventories = inventoryService.getAllInventories(Pageable.unpaged());

        assertNotNull(foundInventories);
        assertEquals(inventories.size(), foundInventories.getTotalElements());
        assertEquals("Pen", foundInventories.getContent().get(0).getItem().getName());
        assertEquals(5, foundInventories.getContent().get(0).getItem().getPrice());
        assertEquals(5, foundInventories.getContent().get(0).getQty());
    }

    @Test
    void testGetInventoryById_Success() {
        Inventory inventory = new Inventory(1L, new Item(1L, "Pen", 5), 5, "T");
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));

        Inventory foundInventory = inventoryService.getInventoryById(1L);

        assertNotNull(foundInventory);
        assertEquals(5, foundInventory.getQty());
        assertEquals("Pen", foundInventory.getItem().getName());
    }

    @Test
    void testGetInventoryById_NotFound() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.getInventoryById(1L));
    }

}
