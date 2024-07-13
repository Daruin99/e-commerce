# E-Commerce Website with Spring, Spring MVC, Hibernate, and Thymeleaf

This project is an e-commerce website implementation using Spring Framework, Spring MVC, Hibernate for database management, and Thymeleaf for the frontend. The application supports both customer and admin functionalities, including user registration, item management, shopping cart, order processing, and integration with payment services.

## Features

### Customer Features

1. **User Registration and Email Verification**
    - Customers can register on the website and must verify their accounts to activate their accounts.
    - Before email verification, the customer account is deactivated. After verification, it becomes activated.
    
2. **Account Suspension and Reactivation**
    - After three incorrect password attempts, a customer's account is locked.
    - Customers must enter their email to receive a reactivation email with a new password reset link.

3. **Item Search and Filter**
    - Logged-in customers can search for items by name or category and view a list of matching items.
    - Items can be filtered by price (high to low or low to high) and by most sold.

4. **Shopping Cart Management**
    - Customers can add, update, and delete items in their shopping cart.
    - The cart retains items even after session expiration or logging out and back in.

5. **Order Preview and Submission**
    - Customers can preview their final order, check out, and submit it for delivery.

6. **Order History**
    - Customers can view their order history, sorted by date, with item details such as name, image, category, price, and rating.

7. **Payment Integration**
    - Customers can pay using their credit card, integrated with a payment gateway through RESTful web services.

### Admin Features

1. **Admin Management**
    - Admins can log in and manage other admins by adding, updating, and deleting admin accounts.
    - Admins can search for other admins by their name.

2. **Item Management**
    - Admins can add, update, delete, and list all items available on the e-commerce website.

### Payment Service

1. **Card Validation Service**
    - A RESTful API that validates card details including number, PIN, and expiration date.

2. **Payment Processing Service**
    - Another RESTful API that processes payments by validating the card's balance and completing the transaction.

## Technology Stack

- **Frontend**: Thymeleaf
- **Backend**: Spring, Spring MVC, Hibernate
- **Database**: MySQL
- **Payment Services**: RESTful APIs, Feign Client for integration

## Installation

1. **Clone the Repository**
    ```sh
    git clone https://github.com/yourusername/e-commerce-website.git
    cd e-commerce-website
    ```

2. **Database Setup**
    - Ensure MySQL is installed and running.
    - Create a database named `ecommerce_db`.
    - Configure the database connection in `src/main/resources/application.properties`.

3. **Build and Run the Application**
    ```sh
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

4. **Access the Application**
    - Open a browser and navigate to `http://localhost:8080`.

## Usage

### Customer Workflow

1. Register an account and verify via email.
2. Log in and search for items.
3. Add items to the shopping cart.
4. Filter items by price (high to low, low to high) or by most sold.
5. Proceed to checkout and choose a payment method.
6. Complete payment and view order history.

### Admin Workflow

1. Log in with admin credentials.
2. Manage admin accounts, including searching for admins by name.
3. Manage items on the website.

## API Documentation

The project includes two RESTful APIs for card validation and payment processing:

1. **Card Validation Service**
    - **Endpoint**: `/api/validate-card`
    - **Method**: POST
    - **Request Body**: `{ "cardNumber": "xxxx-xxxx-xxxx-xxxx", "pin": "1234", "expirationDate": "MM/YY" }`
    - **Response**: `{ "valid": true/false, "message": "validation message" }`

2. **Payment Processing Service**
    - **Endpoint**: `/api/process-payment`
    - **Method**: POST
    - **Request Body**: `{ "cardNumber": "xxxx-xxxx-xxxx-xxxx", "amount": 100.00 }`
    - **Response**: `{ "success": true/false, "message": "payment message" }`

## Contribution Guidelines

1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Commit your changes with descriptive messages.
4. Push your branch and create a pull request.



