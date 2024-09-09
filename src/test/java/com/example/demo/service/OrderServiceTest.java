package com.example.demo.service;

import com.example.demo.exception.InsufficientStockException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Inventory;
import com.example.demo.model.Item;
import com.example.demo.model.Order;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private OrderService orderService;

    public OrderServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_Success() {
        Item item = new Item(1L, "Pen", 5);
        Inventory inventory = new Inventory(1L, item, 5, "T");
        Order order = new Order(1L, item, 2, 0);


        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(inventoryRepository.findRemainingStockByItem(item)).thenReturn(Optional.of(inventory));
        when(orderRepository.save(order)).thenReturn(order);

        Order savedOrder = orderService.createOrder(order);

        assertNotNull(savedOrder);
        assertEquals(5, inventory.getQty());
        assertEquals(2, savedOrder.getQty());
        assertEquals(10, savedOrder.getPrice()); // 2 * 5
    }

    @Test
    void testCreateOrder_InsufficientStock() {
        Item item = new Item(1L, "Pen", 5);
        Inventory inventory = new Inventory(1L, item, 5, "T");

        Order order = new Order(1L, item, 10, 50); // order exceeds stock

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(inventoryRepository.findRemainingStockByItem(item)).thenReturn(Optional.of(inventory));

        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(order));
    }

    @Test
    void testGetOrderById_Success() {
        Item item = new Item(1L, "Pen", 5);
        Order order = new Order(1L, item, 2, 10);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(1L);

        assertNotNull(foundOrder);
        assertEquals(1L, foundOrder.getOrderNo());
        assertEquals(2, foundOrder.getQty());
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
    }
}
