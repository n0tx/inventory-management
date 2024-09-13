# Inventory Management Application

![image](https://github.com/user-attachments/assets/7b440303-4f76-4279-8c09-db9bf78d9fa8)


- Java 17
- Spring Boot
- Rest API
- Spring Data JPA
- Lombok
- Validation
- H2DB
- Relational of table : Item, Inventory, Order
- Inventory Stock Item Logic
- Insufficent Stock Exception
- List with Pagination

This application can register new items, add items to the inventory, and place orders for items which will reduce the stock in the inventory

## How To Run This Application
- Git clone
```
$git clone https://github.com/n0tx/inventory-management.git
```
- Run Spring Boot
```
$cd inventory-management
$pwd
/inventory-management
$./mvnw spring-boot:run
```
### Item Endpoint

- List Item \
GET http://localhost:8080/api/items

- Create Item \
POST http://localhost:8080/api/items
```
{
    "name": "Box",
    "price": 5
}
```
- Item Detail \
GET http://localhost:8080/api/items/{itemId}

- Update Item \
PUT http://localhost:8080/api/items/{itemId}
```
{
    "name": "Box",
    "price": 6
}
```
- Delete Item \
DELETE http://localhost:8080/api/items/{itemId}

- List Item With Pagination \
GET http://localhost:8080/api/items?page=1

- List Item With Stock \
GET http://localhost:8080/api/items/with-stock

- List Item Detail With Stock \
GET http://localhost:8080/api/items/1/with-stock

### Inventory Endpoint

- List Inventory \
GET http://localhost:8080/api/inventories

- Create Inventory \
POST http://localhost:8080/api/inventories
```
{
    "item": {
        "id": 1,
        "name": "Box",
        "price": 6
    },
    "qty": 5
    "type": "T"
}
```
- Inventory Detail \
GET http://localhost:8080/api/inventories/{inventoryId}

- Update Inventory \
PUT http://localhost:8080/api/inventories/{inventoryId}
```
{
    "item": {
            "id": 1,
            "name": "Box",
            "price": 6
        },
        "qty": 2
        "type": "W"
}
```
### Order Endpoint

- List Order \
GET http://localhost:8080/api/orders

- Create Order \
POST http://localhost:8080/api/orders
```
{
    "item": {
        "id": 1,
        "name": "Box",
        "price": 6
    },
    "qty": 2
    "price": 12
}
```
- Order Detail \
GET http://localhost:8080/api/orders/{orderId}

- Update Order \
PUT http://localhost:8080/api/orders/{orderId}
```
{
    "item": {
            "id": 1,
            "name": "Box",
            "price": 6
        },
        "qty": 1
        "price": 6
}
```
- Delete Order \
DELETE http://localhost:8080/api/orders/{orderId}
