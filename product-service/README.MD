# Mankind Matrix Product Service

A microservice for managing products with MySQL database integration.

## Overview

This service provides REST API endpoints for complete product data management, supporting full CRUD (Create, Read, Update, Delete) operations.

## Setup Guide

### Prerequisites

- **Java**: JDK 17
- **MySQL**: 8.0 or newer
- **Git**: For version control and repository cloning

### Installation Steps

#### 1. Install Java (JDK 17)

<details>
<summary><b>macOS</b></summary>

1. Install Homebrew (if not already installed)
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

2. Install Java using Homebrew
```bash
brew install openjdk@17
```

3. Create Java symlink
```bash
sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```
</details>

<details>
<summary><b>Windows</b></summary>

1. Download OpenJDK 17 from [Adoptium](https://adoptium.net/)
2. Run the installer
3. Configure environment: Set JAVA_HOME in System Environment Variables
</details>

<details>
<summary><b>Linux (Ubuntu/Debian)</b></summary>

1. Update system packages
```bash
sudo apt update
```

2. Install Java JDK
```bash
sudo apt install openjdk-17-jdk
```
</details>

#### 2. Install MySQL

<details>
<summary><b>macOS</b></summary>

1. Install MySQL using Homebrew
```bash
brew install mysql
```

2. Start MySQL service
```bash
brew services start mysql
```
</details>

<details>
<summary><b>Windows</b></summary>

1. Download MySQL Installer from [MySQL Website](https://dev.mysql.com/downloads/installer/)
2. Choose "Server only" or "Custom" installation type
3. Follow the setup wizard (remember to note your root password)
</details>

<details>
<summary><b>Linux (Ubuntu/Debian)</b></summary>

Install MySQL Server:
```bash
sudo apt install mysql-server
```
</details>

#### 3. Verify Installation

<details>
<summary>Check installed versions</summary>

```bash
# Verify Java version (should display version 17.x)
java --version

# Verify MySQL version
mysql --version
```
</details>

## Project Setup

### 1. Clone the Repository

```bash
git clone https://github.com/rebeccayilma/Mankind_Matrix_Product_Service.git
```

### 2. Configure Environment

1. Copy the example environment file:
   ```bash
   cp .env.example .env
   ```

### 3. Database Configuration

If you haven't set up the database yet, please follow the [Database Setup Guide](scripts/README.MD).

Update the `.env` file with your database connection details:

```properties
# Database Configuration
DB_HOST=mankind-matrix-db.cd0qkick6gy2.us-west-1.rds.amazonaws.com
DB_PORT=3306
DB_NAME=mankind_matrix_db
DB_USERNAME=matrix_user
DB_PASSWORD=matrix_pass

# Database Connection Properties
DB_CONNECT_TIMEOUT=60000
DB_SOCKET_TIMEOUT=60000
DB_USE_SSL=false
DB_ALLOW_PUBLIC_KEY_RETRIEVAL=true
DB_SERVER_TIMEZONE=UTC
DB_AUTO_RECONNECT=true
DB_FAIL_OVER_READ_ONLY=false

# Hikari Connection Pool Settings
DB_HIKARI_MAX_POOL_SIZE=10
DB_HIKARI_MIN_IDLE=5
DB_HIKARI_CONNECTION_TIMEOUT=60000
DB_HIKARI_IDLE_TIMEOUT=300000
DB_HIKARI_MAX_LIFETIME=1200000
```

### 4. Start the Application

Run the following command to start the service:

```bash
./mvnw spring-boot:run
```

### 5. Access the API

The service will be available at:
- API Endpoint: `http://localhost:8080`
- API Documentation: `http://localhost:8080/swagger-ui/index.html`

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [MySQL Documentation](https://dev.mysql.com/doc/)