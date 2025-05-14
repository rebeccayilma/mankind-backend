# Mankind Matrix AI Backend Monorepo

This repository contains the backend microservices for the **Mankind Matrix AI** platform.

## Services

### `user-service/`
- Handles user registration, login, and authentication
- Secured using JWT (JSON Web Tokens)
- Role-based access control
- Centralized exception handling and error responses

### `product-service/`
- Manages product catalog with full CRUD operations
- Supports category-based filtering and searching
- Includes sample SQL scripts and Postman collection

---

##  Build Instructions

To build **all services at once** from the root:

```bash
mvn clean install
```

Or build each microservice individually:

```bash
cd user-service
./mvnw clean install

cd ../product-service
./mvnw clean install
```

---

## Running Services Locally

Each service is a Spring Boot application and runs independently:

```bash
# Start user-service
cd user-service
./mvnw spring-boot:run
```

```bash
# Start product-service
cd ../product-service
./mvnw spring-boot:run
```

They run on separate ports and are accessible at:

- `http://localhost:8081/swagger-ui.html` (user-service)
- `http://localhost:8080/swagger-ui.html` (product-service)

---

## Monorepo Structure

```
mankind-backend/
├── user-service/         # Handles user authentication & management
├── product-service/      # Handles product catalog functionality
├── README.md             # Project overview and instructions
```


---

##  Contributing

Create a branch for the feature you are working on and when done, create a Pull request and share it for review before merging.
