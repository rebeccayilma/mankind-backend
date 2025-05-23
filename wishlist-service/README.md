# Wishlist Service

## Overview
The Wishlist Service is a microservice component of the Mankind Matrix backend system. It provides functionality for users to create and manage wishlists of products they are interested in.

## Features
- Add products to a user's wishlist
- Remove products from a wishlist
- View a user's wishlist
- Check if a product is in a user's wishlist

## Documentation
Comprehensive documentation for this service is available in the [docs](./docs) directory:

- [Documentation Index](./docs/index.md) - Start here for all documentation
- [API Documentation](./docs/API-DOCUMENTATION.md) - Detailed API endpoint documentation
- [Technical Architecture](./docs/TECHNICAL-ARCHITECTURE.md) - Technical design and architecture

## Quick Start

### Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Running the Service
1. Configure the database connection in `src/main/resources/application.yml`
2. Build the service:
   ```bash
   mvn clean package
   ```
3. Run the service:
   ```bash
   java -jar target/wishlist-service-0.0.1-SNAPSHOT.jar
   ```
4. Access the Swagger UI at: http://localhost:8082/swagger-ui.html

## API Endpoints
- `POST /api/wishlist` - Add item to wishlist
- `GET /api/wishlist/{userId}` - Get user's wishlist
- `DELETE /api/wishlist` - Remove item from wishlist
- `GET /api/wishlist/check` - Check if item is in wishlist

## Example Requests
Example API requests can be found in the [api/requests.http](./api/requests.http) file.

## Technology Stack
- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- MySQL Database
- Swagger/OpenAPI for API documentation
- Maven for dependency management and building