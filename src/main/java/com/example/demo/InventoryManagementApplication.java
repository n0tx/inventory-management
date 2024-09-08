package com.example.demo;

import com.example.demo.model.Inventory;
import com.example.demo.model.Item;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryManagementApplication implements CommandLineRunner {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private OrderRepository orderRepository;

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		if (itemRepository.count() == 0) {
			// add items
			Item pen = itemRepository.save(new Item(1L, "Pen", 5));
			Item book = itemRepository.save(new Item(2L, "Book", 10));
			Item bag = itemRepository.save(new Item(3L, "Bag", 30));
			Item pencil = itemRepository.save(new Item(4L, "Pencil", 3));
			Item shoe = itemRepository.save(new Item(5L, "Shoe", 45));
			Item box = itemRepository.save(new Item(6L, "Box", 5));
			Item cap = itemRepository.save(new Item(7L, "Cap", 25));

			// add inventories
			inventoryRepository.save(new Inventory(1L, pen, 5, "T"));
			inventoryRepository.save(new Inventory(2L, book, 10, "T"));
			inventoryRepository.save(new Inventory(3L, bag, 30, "T"));
			inventoryRepository.save(new Inventory(4L, pencil, 3, "T"));
			inventoryRepository.save(new Inventory(5L, shoe, 45, "T"));
			inventoryRepository.save(new Inventory(6L, box, 5, "T"));
			inventoryRepository.save(new Inventory(7L, cap, 25, "T"));

			System.out.println("Initial data for Item, Inventory table successfully inserted...");
		} else {
			System.out.println("Data already exists in the table");
		}
	}
}
