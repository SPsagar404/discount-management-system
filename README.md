# Discount Management System

## Overview

The **Discount Service API** is a Spring Boot application that allows applying discounts to products. The application supports percentage and flat discounts, as well as seasonal discounts based on predefined conditions. It includes logging, exception handling, and proper API documentation.

## Features

- Apply discounts to products (percentage or flat rate)
- Check product stock before applying discounts
- Seasonal discounts based on predefined discount rates
- Exception handling with a global exception handler
- Detailed logging using SLF4J

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Lombok
- SLF4J (Logging)
- Postman (for API testing)

## Setup Instructions

### Prerequisites

Ensure you have the following installed:

- Java 17+
- Maven
- MySQL Database

### Steps to Run

1. Clone the repository:
```sh
   git clone https://github.com/SPsagar404/discount-management-system.git
   cd discount-management-system
```

2. Update application.properties with your MySQL database details:
```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/discount_db
   spring.datasource.username=root
   spring.datasource.password=password
   spring.jpa.hibernate.ddl-auto=update
```

3. Build and run the application:
```sh
   mvn clean install
   mvn spring-boot:run
```

## SQL Seed Data

### Insert Seasonal Discounts
```sql
INSERT INTO seasons (id, name, start_date, end_date, discount_rate) VALUES
(1, 'Summer Sale', '2025-06-01', '2025-06-30', 0.20),  -- 20% discount
(2, 'Winter Sale', '2025-12-01', '2025-12-31', 0.30),  -- 30% discount
(3, 'Spring Festival', '2025-04-01', '2025-04-30', 0.15); -- 15% discount
```
### Insert Products
```sql
INSERT INTO products (id, name, price, quantity, seasonal_discount_active, season_id) VALUES
(1, 'Laptop', 1000.00, 10, TRUE, 1),  
(2, 'Smartphone', 800.50, 15, FALSE, NULL),
(3, 'Tablet', 499.99, 20, TRUE, 2),   
(4, 'Smartwatch', 249.75, 25, FALSE, NULL),
(5, 'Wireless Earbuds', 149.49, 30, TRUE, 1),  
(6, 'Gaming Console', 499.00, 8, TRUE, 3),  
(7, 'Bluetooth Speaker', 99.99, 50, FALSE, NULL),  
(8, 'DSLR Camera', 1500.00, 5, TRUE, 2),  
(9, 'Monitor', 399.00, 12, FALSE, NULL),  
(10, 'Keyboard', 49.99, 40, TRUE, 1),  
(11, 'Mouse', 29.99, 35, FALSE, NULL),  
(12, 'External Hard Drive', 89.99, 20, TRUE, 3),  
(13, 'Smart TV', 999.99, 10, FALSE, NULL),  
(14, 'VR Headset', 299.99, 7, TRUE, 1),  
(15, 'Wireless Charger', 19.99, 60, FALSE, NULL),  
(16, 'Fitness Tracker', 79.99, 15, TRUE, 2),  
(17, 'Microwave Oven', 249.99, 5, FALSE, NULL),  
(18, 'Coffee Maker', 129.99, 10, TRUE, 3),  
(19, 'Air Conditioner', 899.99, 8, TRUE, 1),  
(20, 'Refrigerator', 1100.00, 6, TRUE, 2),  
(21, 'Headphones', 199.99, 20, FALSE, NULL),  
(22, 'Electric Toothbrush', 69.99, 30, TRUE, 3),  
(23, 'Robot Vacuum', 399.99, 5, FALSE, NULL),  
(24, 'Portable Projector', 299.99, 8, TRUE, 1),  
(25, 'Washing Machine', 750.00, 4, TRUE, 2),  
(26, 'Smart Home Hub', 149.99, 12, FALSE, NULL),  
(27, 'Streaming Device', 39.99, 50, TRUE, 3),  
(28, 'e-Reader', 129.99, 10, FALSE, NULL),  
(29, 'Portable Power Bank', 59.99, 25, TRUE, 1),  
(30, 'Wireless Security Camera', 179.99, 15, FALSE, NULL);
```
### Commit Transaction
```sql
COMMIT;
```

## API Endpoints

### 1. Apply Discount

  - **Endpoint:** `POST /product/discount`  
  - **Description:** Apply a discount to a product.  
  - **Method:** `POST`  
  - **Request Body:**
    ```json
    {
      "productId": 1,
      "discountType": "PERCENTAGE",
      "discountValue": 10,
      "seasonalDiscountActive": true
    }
    ```
  - **Response:**
    ```json
    {
      "productId": 1,
      "productName": "Laptop",
      "price": 900.00
    }
    ```
### 2. Get Product Details

  - **Endpoint:** `GET /product/{id}`  
  - **Description:** Fetch product details by product ID.  
  - **Method:** `GET`  
  - **Response:**
    ```json
    {
      "productId": 1,
      "productName": "Laptop",
      "price": 900.00
    }
    ```

## Global Exception Handling  
This application includes a **Global Exception Handler** to manage errors gracefully.  

### Exception Scenarios  

| Exception                     | HTTP Status     | Message                         |
| ----------------------------- | --------------- | ------------------------------- |
| ProductNotFoundException      | 404 Not Found   | Product not found with ID: {id} |
| ProductOutOfStockException    | 400 Bad Request | Product is out of stock.        |
| DiscountTypeNotFoundException | 400 Bad Request | Invalid discount type provided. |


## Logging  

- **INFO** logs track API calls and discount processing.  
- **DEBUG** logs track price calculations.  
- **ERROR** logs track exceptions.  

### Log Example  

```log
INFO - Processing discount for product ID: 1
DEBUG - Applied percentage discount: 10%. New price: 900.00
INFO - Returning ProductResponse: Product ID: 1, Name: Laptop, Final Price: $900.00
```

## Testing with Postman  

1. Import the provided Postman collection.  
2. Update environment variables if needed.  
3. Test API requests for applying discounts and fetching products.  


