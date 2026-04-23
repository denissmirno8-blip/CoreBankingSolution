# Core Banking Solution (Test Assignment)

A lightweight REST API service for managing banking accounts, balances, and transactions.

The REST API application which allows to create accounts and transactions, to find account or transactions by account id.

# 🧠 System Overview

The service is a RESTful built with the Spring Boot framework. It handles core banking operations with a focus on data consistency and asynchronous processing.

# 🛠 Tech Stack

- Backend: Java 21, SpringBoot
- Persistance: MyBatis, PostgreSQL
- Messaging: RabbitMQ
- Build Tool: Gradle
- Testing: JUnit, H2, Jacoco
- Migrations: Flyway

# 📂 Project Structure

```
.
├── src/main/java/com/example/demo/
│   ├── config/              # Infrastructure configuration (RabbitMQ)
│   ├── controllers/         # REST API Endpoints (Account, Transaction)
│   ├── entities/            # Domain models and Requests
│   ├── exceptions/          # Custom exceptions and Global Handler
│   ├── mappers/             # MyBatis Mapper interfaces with SQL
│   ├── services/            # Business logic implementation
│   └── DemoApplication.java # Application entry point
├── build.gradle             # Build configuration
├── docker-compose.yml       # Docker services orchestration
├── Dockerfile               # Application container image
└── README.md                # Project documentation
```


# 🐳 Deployment & Environment

The project is fully dockerized. No local installation is required except for Docker Desktop..

## Prerequsties

- Docker Desktop(installing and opening)
- Docker Compose

## Quick start

1. Clone the repository:
```
    git clone https://github.com/denissmirno8-blip/CoreBankingSolution.git
```

2. Launch all services:
```
    docker-compose up -d
```

3. Access the APIs:
- The application will be available at 
http://localhost:8080
- The RabbitMQ will be available at http://localhost:15672 (Username: user, Password: password)

4. Testing:
- Run tests: `./gradlew test` (or `gradlew test` for Windows)
- Code coverage report: `/demo/build/reports/jacoco/index.html` (open in your browser)

# 🧭 Usage guide
This section provides detailed instructions on how to interact with the Banking Management System API

## API Endpoints
The system provides a RESTful API to manage entities (Account, Transaction). 

### Standard Operations
#### Standard CRUD Operations & Pagination
| Method   | Endpoint                        | Description                                  |
|:---------|:--------------------------------|:---------------------------------------------|
| **POST** | `/api/accounts`                 | Create a new account.                       |
| **GET**  | `/api/accounts/{id}`            | Find a specific account by its ID.          |
| **POST** | `/api/transactions`             | Create a new transaction                    |
| **GET**  | `/api/transaction/{account_id}` | Retrieve a list of transactions by account ID.|

Create account
- Input:
    * Customer ID
    * Country
    * List of currenicies

Get account
- Input: 
    * Account ID

Create transaction
- Input:
    * Account ID
    * Amount
    * Currency
    * Direction of transaction
    * Description

Get list of transactions
- Input: 
    * Account ID

### Request Examples

**Create Account** (`POST /api/accounts`)
```json
{
  "customerId": 12345,
  "country": "Estonia",
  "currencies": ["EUR", "USD"]
}
```

**Create Transaction** (`POST /api/transactions`)
```json
{
  "accountId": 1,
  "amount": 100.50,
  "currency": "EUR",
  "direction": "IN",
  "description": "Salary deposit"
}
```


# Answers about the project

## Important choices

## Application scaling

## Transactions estamation

## AI explanation

## ✉️ Contact
Created by Denis Smirnov - (https://www.linkedin.com/in/denis-smirnov-628955221/)