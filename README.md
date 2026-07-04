# ✈️ Airport API (Sprint S4)

## Overview

The Airport API is a Spring Boot RESTful web application designed to manage an airport system consisting of **Cities, Airports, Planes, and Passengers**. The application provides full CRUD (Create, Read, Update, Delete) functionality along with several relationship-based endpoints that allow users to retrieve connected data, such as airports within a city or the airports and planes associated with a passenger.

The project follows a layered architecture using Spring Boot, Spring Data JPA, and MySQL.

---

## Features

- RESTful API built with Spring Boot
- Full CRUD operations for:
  - Cities
  - Airports
  - Planes
  - Passengers
- Relationship endpoints including:
  - View airports in a city
  - View airports used by a passenger
  - View planes used by a passenger
  - View airports served by a plane
- Automatic database seeding on application startup
- MySQL database integration
- Postman collection included for endpoint testing

---

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- Postman
- IntelliJ IDEA

---

# Project Structure

```
src
└── main
    ├── java
    │   └── com
    │       ├── controller
    │       ├── model
    │       ├── service
    │       ├── repo
    │       ├── component
    │       └── AirportApiApplication.java
    │
    └── resources
        ├── application.properties
        └── data.sql

SprintS4_Postman_Collection.json
README.md
```

### Main Packages

| Package | Description |
|----------|-------------|
| controller | REST API endpoints |
| model | JPA Entity classes |
| service | Business logic |
| repo | Spring Data JPA repositories |
| component | Database seed data |

---

# Prerequisites

Before running the project, ensure you have:

- Java 17 or newer
- Maven (or Maven Wrapper)
- MySQL Server
- IntelliJ IDEA (recommended)
- Postman (optional for testing)

---

# Database Configuration

Update your `application.properties` file with your own MySQL credentials.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/airportdb
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

For security, it is recommended to store database credentials using environment variables instead of committing them to source control.

---

# Running the Application

## IntelliJ IDEA

1. Open the project.
2. Wait for Maven dependencies to download.
3. Run:

```
AirportApiApplication
```

or

```
SprintS4Application
```

---

## Using Maven

Run:

```bash
mvn spring-boot:run
```

or

```bash
./mvnw spring-boot:run
```

---

## Build the Project

```bash
mvn clean package
```

Run the generated JAR:

```bash
java -jar target/<artifact>.jar
```

---

# Database Seeding

The application includes two methods of loading sample data.

### DataSeeder

```
src/main/java/com/component/DataSeeder.java
```

Automatically inserts sample Cities, Airports, Planes, and Passengers if the database is empty.

### data.sql

```
src/main/resources/data.sql
```

Contains SQL INSERT statements for sample data.

> **Note:** Only use one seeding method to avoid duplicate records.

---

# Base URL

```
http://localhost:8080
```

---

# API Endpoints

## Airports

| Method | Endpoint | Description |
|----------|----------------|---------------------------|
| GET | /airports | Get all airports |
| GET | /airports/{id} | Get airport by ID |
| POST | /airports | Create airport |
| PUT | /airports/{id} | Update airport |
| DELETE | /airports/{id} | Delete airport |

---

## Cities

| Method | Endpoint | Description |
|----------|--------------------------|----------------------------|
| GET | /cities | Get all cities (paged) |
| GET | /cities/{id} | Get city by ID |
| POST | /cities | Create city |
| PUT | /cities/{id} | Update city |
| DELETE | /cities/{id} | Delete city |
| GET | /cities/{id}/airports | Get airports in city |

---

## Passengers

| Method | Endpoint | Description |
|----------|----------------------------------|-----------------------------|
| GET | /api/passengers | Get all passengers |
| GET | /api/passengers/{id} | Get passenger by ID |
| POST | /api/passengers | Create passenger |
| PUT | /api/passengers/{id} | Update passenger |
| DELETE | /api/passengers/{id} | Delete passenger |
| GET | /api/passengers/{id}/planes | Get passenger planes |
| GET | /api/passengers/{id}/airports | Get passenger airports |

---

## Planes

| Method | Endpoint | Description |
|----------|------------------------|----------------------------|
| GET | /planes | Get all planes |
| GET | /planes/{id} | Get plane by ID |
| POST | /planes | Create plane |
| PUT | /planes/{id} | Update plane |
| DELETE | /planes/{id} | Delete plane |
| GET | /planes/{id}/airports | Get airports served by plane |

---

# Example Requests

### Get Cities

```bash
curl http://localhost:8080/cities?page=0&size=20
```

---

### Get Airports in City

```bash
curl http://localhost:8080/cities/1/airports
```

---

### Create Passenger

```bash
curl -X POST http://localhost:8080/api/passengers \
-H "Content-Type: application/json" \
-d '{
  "firstName":"Sam",
  "lastName":"Lee",
  "phoneNumber":"555-1212"
}'
```

---

### Create Plane

```bash
curl -X POST http://localhost:8080/planes \
-H "Content-Type: application/json" \
-d '{
  "type":"Boeing 787",
  "airlineName":"ExampleAir",
  "numOfPassengers":240
}'
```

---

# Testing with Postman

A Postman collection is included with the project.

```
SprintS4_Postman_Collection.json
```

Import the collection into Postman to quickly test every API endpoint.

---

# Troubleshooting

### Duplicate Seed Data

If duplicate data appears:

- Disable `data.sql`
- or disable `DataSeeder`

Only one should be used.

---

### Database Connection Errors

Verify:

- MySQL is running
- Database exists
- Username and password are correct
- JDBC URL is correct

---

### JSON Serialization Issues

If circular reference errors occur, verify the entity annotations:

- `@JsonManagedReference`
- `@JsonBackReference`

---

# Future Improvements

Possible enhancements include:

- DTOs and request validation
- Unit and integration testing
- Swagger/OpenAPI documentation
- Docker support
- CI/CD pipeline
- Environment variable configuration
- Authentication and authorization
- H2 database profile for testing

---

# Authors

Sprint S4 Development Team
Matt Reid
Keith Bishop
Charles Rubia

Built using Spring Boot, Spring Data JPA, and MySQL.

---

# License

This project was created for educational purposes as part of a Spring Boot API development assignment.

