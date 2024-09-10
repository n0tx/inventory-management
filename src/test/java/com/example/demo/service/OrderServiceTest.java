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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
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
        assertEquals(3, inventory.getQty()); // stock (5) - order (2)
        assertEquals(2, savedOrder.getQty()); // order (2)
        assertEquals(10, savedOrder.getPrice()); // order (2) * itemPrice (5)
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
    void testGetAllOrders() {
        Item item = new Item(1L, "Pen", 5);
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1L, item, 2, 10));
        orders.add(new Order(2L, item, 3, 15));

        when(orderRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(orders));

        Page<Order> foundOrders = orderService.getAllOrders(Pageable.unpaged());

        assertNotNull(foundOrders);
        assertEquals(orders.size(), foundOrders.getTotalElements());
        assertEquals("Pen", foundOrders.getContent().get(0).getItem().getName());
        assertEquals(2, foundOrders.getContent().get(0).getQty());
        assertEquals(10, foundOrders.getContent().get(0).getPrice());
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
    }
}
