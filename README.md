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
- **Docker**: For containerization
- **Docker Compose**: For multi-container orchestration

### Installation Steps

#### 1. Install Docker and Docker Compose

<details>
<summary><b>macOS</b></summary>

1. Install Docker Desktop for Mac
   - Download from [Docker's official website](https://www.docker.com/products/docker-desktop)
   - Docker Desktop includes both Docker Engine and Docker Compose
   - Follow the installation wizard

2. Verify installation:
```bash
docker --version
docker-compose --version
```
</details>

<details>
<summary><b>Windows</b></summary>

1. Install Docker Desktop for Windows
   - Download from [Docker's official website](https://www.docker.com/products/docker-desktop)
   - Ensure WSL 2 is installed (Docker Desktop will prompt if not)
   - Docker Desktop includes both Docker Engine and Docker Compose
   - Follow the installation wizard

2. Verify installation:
```bash
docker --version
docker-compose --version
```
</details>

<details>
<summary><b>Linux (Ubuntu/Debian)</b></summary>

1. Install Docker Engine:
```bash
# Update package index
sudo apt-get update

# Install prerequisites
sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

# Add Docker's official GPG key
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Set up the repository
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Install Docker Engine
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

2. Install Docker Compose:
```bash
# Install Docker Compose
sudo apt-get install docker-compose-plugin
```

3. Verify installation:
```bash
docker --version
docker compose version
```
</details>

#### 2. Install Java (JDK 17)

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

#### 3. Install MySQL

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

#### 4. Verify Installation

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

- `http://localhost:8080/swagger-ui/index.html` (product-service)
- `http://localhost:8081/swagger-ui/index.html` (user-service)
- `http://localhost:8082/swagger-ui/index.html` (cart-service)
- `http://localhost:8083/swagger-ui/index.html` (wishlist-service)

---

## Running Services with Docker

To run all services using Docker:

1. Make sure Docker and Docker Compose are installed and running
   ```bash
   # Verify Docker is running
   docker info
   
   # If Docker is not running, start Docker Desktop:
   # - Open Docker Desktop from Applications
   # - Wait for the whale icon in the menu bar to stop animating
   # - Run 'docker info' again to verify it's running
   ```

   > **Note for Apple Silicon (M1/M2) Users**: 
   > The Dockerfiles are configured to use `--platform=linux/amd64` for compatibility.
   > Docker Desktop will automatically handle the emulation.

2. Ensure all `.env` files are properly configured in each service directory
3. From the root directory, run:

```bash
# Build and start all services
docker-compose up --build

# To run in detached mode (background)
docker-compose up -d --build

# To stop all services
docker-compose down
```

The services will be available at:
- `http://localhost:8080/swagger-ui/index.html` (product-service)
- `http://localhost:8081/swagger-ui/index.html` (user-service)
- `http://localhost:8082/swagger-ui/index.html` (cart-service)
- `http://localhost:8083/swagger-ui/index.html` (wishlist-service)

To check the status of your containers:
```bash
# List running containers
docker-compose ps

# View logs for all services
docker-compose logs

# View logs for a specific service
docker-compose logs product-service
```

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

---

## Deployment

For detailed deployment instructions, including AWS setup and cost-effective deployment options, please refer to [DEPLOY.md](DEPLOY.md).
