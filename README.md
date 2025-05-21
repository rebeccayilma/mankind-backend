# Mankind Matrix AI Backend

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
# Setup Guide

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
---

## Project Setup


### 1. Clone the Repository

```bash
git clone https://github.com/rebeccayilma/mankind-backend.git
```

### 2. Configure Environment

1. Copy the example environment file inside of each microservice folder
2. Update the `.env` file with your database connection details. Example bellow:

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

### 3. Database Configuration

If you haven't set up the database yet, please follow the [Database Setup Guide](product-service/scripts/README.MD).

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
