package com.example.demo.service;

import com.example.demo.exception.InsufficientStockException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Inventory;
import com.example.demo.model.Item;
import com.example.demo.model.Order;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    public Order createOrder(Order order) {
        Item item = itemRepository.findById(order.getItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        Inventory stockInventoryItem = inventoryRepository.findRemainingStockByItem(item)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory for item not found"));

        if (stockInventoryItem.getQty() < order.getQty()) {
            throw new InsufficientStockException(
                    "Insufficient stock (" + stockInventoryItem.getQty() + ") for item: " + item.getName());
        }

        // Subtract stock from inventory
        stockInventoryItem.setQty(stockInventoryItem.getQty() - order.getQty());
        inventoryRepository.save(stockInventoryItem);

        // Calculate total price based on order quantity
        order.setPrice(item.getPrice() * order.getQty());

        return orderRepository.save(order);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + id));

        order.setItem(orderDetails.getItem());
        order.setQty(orderDetails.getQty());
        order.setPrice(orderDetails.getPrice());

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
