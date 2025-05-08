# Mankind Matrix Product Service

A microservice for managing products, using MySQL as the database.

## Overview

This service provides REST API endpoints to manage product data with full CRUD operations.

## Setup Guide

### Prerequisites

- **Java**: JDK 17
- **MySQL**: 8.0 or newer
- **Git**: For cloning the repository

### Installation Steps

#### 1. Install Java (JDK 17)

<details>
<summary><b>macOS</b></summary>

```bash
# Install Homebrew (if not installed)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install with Homebrew
brew install openjdk@17

# Link Java
sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```
</details>

<details>
<summary><b>Windows</b></summary>

1. Download OpenJDK 17 from [Adoptium](https://adoptium.net/)
2. Run the installer
3. Add to PATH: Set JAVA_HOME in Environment Variables
</details>

<details>
<summary><b>Linux (Ubuntu/Debian)</b></summary>

```bash
sudo apt update
sudo apt install openjdk-17-jdk
```
</details>

#### 2. Install MySQL

<details>
<summary><b>macOS</b></summary>

```bash
# Install MySQL
brew install mysql

# Start MySQL service
brew services start mysql

# Secure the installation
mysql_secure_installation

```
</details>

<details>
<summary><b>Windows</b></summary>

1. Download MySQL Installer from [MySQL Website](https://dev.mysql.com/downloads/installer/)
2. Select "Server only" or "Custom" installation
3. Complete the setup wizard (note your root password)
</details>

<details>
<summary><b>Linux (Ubuntu/Debian)</b></summary>

```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```
</details>

#### 3. Verify Installations

```bash
# Check Java version (should show version 17.x)
java --version

# Check MySQL version
mysql --version

# Check if MySQL is running
brew services list | grep mysql

# Stop MySQL service
brew services stop mysql

# Restart MySQL service
brew services restart mysql
```

### Project Setup

#### 1. Clone the Repository

```bash
git clone https://github.com/rebeccayilma/Mankind_Matrix_Product_Service.git
cd Mankind_Matrix_Product_Service
```

#### 2. Database Setup

Ensure MySQL is running on:
- **Host:** `localhost`
- **Port:** `3306`

To verify MySQL is running on the correct host and port:
```bash
# Check if MySQL is running
mysql -u root -p -e "SELECT @@hostname AS 'Host', @@port AS 'Port';"

# Alternative command to check MySQL status
mysqladmin -u root -p ping
```

**Step 1**: Create the database and user

```bash
mysql -u root -p < scripts/setup.sql
```

**Step 2**: Create the product table

```bash
mysql -u root -p < scripts/create_table.sql
```

**Step 3**: Load sample product data

```bash
mysql -u root -p mankind_matrix_db < scripts/sample_product_insert.sql
```

#### 3. Run the Application

```bash
./mvnw spring-boot:run
```

Or using your IDE:
- Open the project and run `ProductServiceApplication.java`

### Testing the API

The service will be available at `http://localhost:8080`

#### Available Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/products/{id}` | GET | Get product by ID |
| `/api/v1/products/category/{category}` | GET | Get products by category |

#### Sample Request

Using cURL:

```bash
curl -X GET http://localhost:8080/api/v1/products/1 -H "Accept: application/json"
```

Using Postman:
1. Create a new GET request
2. Enter URL: `http://localhost:8080/api/v1/products/1`
3. Add header: `Accept: application/json`
4. Click Send

## Troubleshooting

### Common Issues

1. **MySQL Connection Failure**
   - Verify MySQL is running: `brew services list | grep mysql` (macOS) or `systemctl status mysql` (Linux)
   - Check credentials in application properties

2. **Java Version Mismatch**
   - Ensure you're using JDK 17: `java --version`

3. **Port Already in Use**
   - Change the port in `application.properties` if port 8080 is occupied

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [MySQL Documentation](https://dev.mysql.com/doc/)