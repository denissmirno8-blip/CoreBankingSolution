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

## Design decisions and implementation details
For Accounts, I created a dedicated AccountRequest because the input requirements differed from the output. For Transactions I combined input and output logic by using flexible constructor to avoid redundant code.
I extracted Balance into separate class. This decoupling makes it easier to manage multiple currency balances independantly and scale the logic.
I used Flyway for reliable schema management. All initial tables and constraints are defiend in V1__create_tables.sql.

In RabbitMQ I designed a system with 1 exchange and 3 queues for account creation, transaction creation and balance updates. Controllers as producers are sending JSON payloads to the broker to decouple API responses from heavy processing.

I integrated Jacoco to track testing quality. For controller testing I used MockMvc to perform integration tests on REST endpoints and ensuring correct HTTP status codes. Additionally I used lombok to reduce getters and setters.

In the future I should add DELETE and PATCH methods for full resource management. And finalize the RabbiMQ consumer workers to process the background tasks.


## Scalability & High Availability

To ensure that the application can be scaled horizontally, we need follow these principles:

- Stateless arhitecture: The application doesn't store data in local memory. All data is stored in Postgres, and shared state is managed by RabbitMQ.

- Centralized logging: Logs need to be structured by centralized system. This is crucial for monitoring application health across multiple instances.

- Message broker scalability: Using RabbitMQ allows us to decouple services. In the future we can introduce dedicated queues to prioritize high value transactions or balance the load between workers.

## Transactions estamation

On my development machine, the application can consistently handle approximately 181 transactions per second (TPS) with moderate latency.

### User guide

If you want to estimate my application by yourself you need to do next steps.

In cmd put the command to create the account (if account table is empty).

```
    curl -v -X POST http://localhost:8080/api/accounts -H "Content-Type: application/json" -d @acc_data.json
```

After that please put the next command

```
    docker run --rm -v "%cd%:/scripts" alpine/bombardier -n 5000 -c 50 -m POST -f /scripts/data.json -H "Content-Type: application/json" http://host.docker.internal:8080/api/transactions
```

And finally you can find the statistics and check the transactions estimation.

## AI explanation

In this project, AI was used as a technical consultant and accelerator to ensure high-quality standards and infrastructure consistency. AI helped me in identifying compatible versions of Java 21, MyBatis, and RabbitMQ, significantly reducing time spent on resolving dependency conflicts. AI was used to research standard industry patterns for Docker configuration and REST API structure.


## ✉️ Contact
Created by Denis Smirnov - (https://www.linkedin.com/in/denis-smirnov-628955221/)