package com.example.demo.service;

import com.example.demo.dto.ItemWithStock;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private ItemService itemService;

    public ItemServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetItemById_Success() {
        Item item = new Item(1L, "Pen", 5);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item foundItem = itemService.getItemById(1L);

        assertNotNull(foundItem);
        assertEquals(1L, foundItem.getId());
        assertEquals("Pen", foundItem.getName());
        assertEquals(5, foundItem.getPrice());
    }

    @Test
    void TestGetAllItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "Pen", 5));
        items.add(new Item(2L, "Book", 10));
        items.add(new Item(3L, "Bag", 30));
        items.add(new Item(4L, "Pencil", 3));
        items.add(new Item(5L, "Shoe", 45));

        when(itemRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(items));

        Page<Item> foundItems = itemService.getAllItems(Pageable.unpaged());

        assertNotNull(foundItems);
        assertEquals(items.size(), foundItems.getTotalElements());

        assertEquals("Pen", foundItems.getContent().get(0).getName());
        assertEquals(5, foundItems.getContent().get(0).getPrice());

    }

    @Test
    void testGetItemsWithStock() {

        List<Item> items = new ArrayList<>();

        // items
        Item pen = new Item(1L, "Pen", 5);
        Item book = new Item(2L, "Book", 10);
        Item bag = new Item(3L, "Bag", 30);

        items.add(pen);
        items.add(book);
        items.add(bag);

        // inventories
        Inventory inventoryPen = new Inventory(1L, pen, 5, "T");
        Inventory inventoryBook = new Inventory(2L, book, 10, "T");
        Inventory inventoryBag = new Inventory(3L, bag, 30, "T");

        List<ItemWithStock> itemWithStocks = new ArrayList<>();

        itemWithStocks.add(new ItemWithStock(pen, inventoryPen.getQty()));
        itemWithStocks.add(new ItemWithStock(book, inventoryBook.getQty()));
        itemWithStocks.add(new ItemWithStock(bag, inventoryBag.getQty()));

        when(itemRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(items));
        when(inventoryRepository.calculateRemainingStock(pen)).thenReturn(Optional.of(inventoryPen.getQty()));
        when(inventoryRepository.calculateRemainingStock(bag)).thenReturn(Optional.of(inventoryBag.getQty()));
        when(inventoryRepository.calculateRemainingStock(book)).thenReturn(Optional.of(inventoryBook.getQty()));

        Page<ItemWithStock> foundItemWithStocks = itemService.getItemsWithStock(Pageable.unpaged());

        assertNotNull(foundItemWithStocks);

        assertEquals("Pen", foundItemWithStocks.getContent().get(0).getName());
        assertEquals(5, foundItemWithStocks.getContent().get(0).getRemainingStock());

        assertEquals("Book", foundItemWithStocks.getContent().get(1).getName());
        assertEquals(10, foundItemWithStocks.getContent().get(1).getRemainingStock());

        assertEquals("Bag", foundItemWithStocks.getContent().get(2).getName());
        assertEquals(30, foundItemWithStocks.getContent().get(2).getRemainingStock());
    }

    @Test
    void testGetItemWithStock() {

        // items
        Item pen = new Item(1L, "Pen", 5);

        // inventories
        Inventory inventoryPen = new Inventory(1L, pen, 5, "T");

        ItemWithStock itemWithStock = new ItemWithStock(pen, inventoryPen.getQty());


        when(itemRepository.findById(1L)).thenReturn(Optional.of(pen));
        when(inventoryRepository.calculateRemainingStock(pen)).thenReturn(Optional.of(inventoryPen.getQty()));

        ItemWithStock foundItemWithStock = itemService.getItemByIdWithStock(1L);

        assertNotNull(foundItemWithStock);
        assertEquals(1L, foundItemWithStock.getId());
        assertEquals("Pen", foundItemWithStock.getName());
        assertEquals(5, foundItemWithStock.getPrice());
        assertEquals(5, foundItemWithStock.getRemainingStock());
    }

    @Test
    void testGetItemById_NotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemService.getItemById(1L));
    }

    @Test
    void testSaveItem() {
        Item item = new Item(1L, "Pen", 5);
        when(itemRepository.save(item)).thenReturn(item);

        Item savedItem = itemService.saveItem(item);

        assertNotNull(savedItem);
        assertEquals("Pen", savedItem.getName());
        assertEquals(5, savedItem.getPrice());
    }

    @Test
    void testDeleteItem() {
        itemService.deleteItem(1L);
        verify(itemRepository, times(1)).deleteById(1L);
    }
}
