package com.example.demo.repository;

import com.example.demo.model.Inventory;
import com.example.demo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("SELECT i FROM Inventory i WHERE i.item = :item ORDER BY i.id DESC LIMIT 1")
    Optional<Inventory> findRemainingStockByItem(@Param("item") Item item);

    @Query("SELECT i.qty FROM Inventory i WHERE i.item = :item ORDER BY i.id DESC LIMIT 1")
    Optional<Integer> calculateRemainingStock(@Param("item") Item item);
}
