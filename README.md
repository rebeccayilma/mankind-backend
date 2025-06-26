# Mankind Matrix AI Backend

This repository contains the backend microservices for the **Mankind Matrix AI** platform.

## Clone Repository

1. **Clone the Repository**
   ```bash
   git clone https://github.com/rebeccayilma/mankind-backend.git
   cd mankind-backend
   ```

## Quick Start

This project can be run in two ways:

1. **Local Development** - Run services directly on your machine
   - Requires: JDK 17, MySQL, Maven
   - Best for: Development and debugging
   - [Jump to Local Setup →](#local-development)

2. **Docker Environment** - Run services in containers
   - Requires: Docker and Docker Compose
   - Best for: Production-like environment and quick setup
   - [Jump to Docker Setup →](#docker-environment)

You can choose your preferred method based on your needs. Docker is recommended for most users as it provides a consistent environment and a simpler setup.

## Local Development

### Prerequisites
   <details>
   <summary><b>Java (JDK 17) </b></summary>

      #### macOS
         - Install Homebrew (if not already installed)
         ```bash
         /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
         ```
         - Install Java
         ```bash
         brew install openjdk@17
         ```
         - Create Java symlink
         ```bash
         sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
         ```

      #### Windows
      1. Download OpenJDK 17 from [Adoptium](https://adoptium.net/)
      2. Run the installer
      3. Configure environment: Set JAVA_HOME in System Environment Variables

      #### Linux (Ubuntu/Debian)
      ```bash
      sudo apt update
      sudo apt install openjdk-17-jdk
      ```
   </details>

   <details>
   <summary><b>MySQL</b></summary>

   #### macOS
   ```bash
   brew install mysql
   brew services start mysql
   ```

   #### Windows
   1. Download MySQL Installer from [MySQL Website](https://dev.mysql.com/downloads/installer/)
   2. Choose "Server only" or "Custom" installation type
   3. Follow the setup wizard

   #### Linux (Ubuntu/Debian)
   ```bash
   sudo apt install mysql-server
   ```
   </details>

### Run Service

1. **Configure Environment**
   - Copy `.env.example` on the folder root
   - Paste that file in each microservice directory
   - Rename the file to `.env`
   - Update the database connection details in each `.env` file
   - For detailed database configuration, see [Database Setup](#database-setup) section

2. **Build the Services**

   - Build all services at once, for example:
   ```bash
   mvn clean install
    ```
   - Or build each service individually
   ```bash
   cd user-service
   ./mvnw clean install
   ```
   or 

   ```bash
   cd product-service
   ./mvnw clean install
   ```
   Build Gateway
   ```bash
   cd mankind-gateway-service
   mvn clean install
   ```
   > **Note:** The gateway service uses `mvn clean install` because it does not have a Maven Wrapper (`mvnw`). The other services use `mvn clean install` for consistency and portability.

4. **Run the Services**

   **Recommended: Use the provided script (easiest method)**
   ```bash
   ./scripts/run-all-services.sh
   ```
   
   **Alternative: Manual startup**
   Enter each service folder and run it. 
   For example, run the user-service:

   ```bash
   cd user-service
   ./mvnw spring-boot:run
   ```
   Or run the product service
   ```bash
   cd product-service
   ./mvnw spring-boot:run
   ```

   Run Gateway
   ```bash
   cd mankind-gateway-service
   mvn spring-boot:run
   ```
   > **Note:** The gateway service uses `mvn spring-boot:run` because it does not have a Maven Wrapper (`mvnw`). The other services use `./mvnw spring-boot:run` for consistency and portability.

   **To stop all services:**
   ```bash
   ./scripts/stop-all-services.sh
   ```

### Service Swagger documentation links:
- Gateway: [http://localhost:8085](http://localhost:8085)

### Services documentation

Each service has its detailed documentation. Click on the service name to view its specific README:

#### Available Services
- [`user-service/`](user-service/README.md)
- [`product-service/`](product-service/README.md)
- [`cart-service/`](cart-service/README.md)
- [`wishlist-service/`](wishlist-service/README.md)
- [`payment-service/`](payment-service/README.md)

------------

## Docker Environment
### Prerequisites
   <details>
   <summary><b>Docker and Docker Compose</b></summary>

   #### macOS
   1. Install Docker Desktop for Mac
      - Download from [Docker's official website](https://www.docker.com/products/docker-desktop)
      - Docker Desktop includes both Docker Engine and Docker Compose
      - Follow the installation wizard

   #### Windows
   1. Install Docker Desktop for Windows
      - Download from [Docker's official website](https://www.docker.com/products/docker-desktop)
      - Ensure WSL 2 is installed (Docker Desktop will prompt if not)
      - Docker Desktop includes both Docker Engine and Docker Compose
      - Follow the installation wizard

   #### Linux (Ubuntu/Debian)
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

   # Install Docker Engine and Docker Compose
   sudo apt-get update
   sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
   ```
   </details>

### Run Service

1. **Verify Docker Installation**
   - Check if Docker is running
   ```bash
   docker info
   ```
2. **Configure Environment**
   - Copy `.env.example` to the folder root
   - Paste that file in each microservice directory
   - Rename the file to `.env`
   - Update the database connection details in each `.env` file
   - For detailed database configuration, see [Database Setup](#database-setup) section

3. **Run All Services**
   - Build and start all services
   ```bash
   docker-compose up --build
   ```

4. **Manage Docker Services**
   - Check running containers
   ```bash
   docker-compose ps
   ```

   - View logs for all services
   ```bash
   docker-compose logs
    ```

   - View logs for a specific service
   ```bash
   docker-compose logs product-service
    ```

   - Stop all services
   ```bash
   docker-compose down
   ```

### Service Swagger documentation links:
- Gateway: [http://localhost:8085](http://localhost:8085)


### Services documentation

Each service has its detailed documentation. Click on the service name to view its specific README:

#### Available Services
- [`user-service/`](user-service/README.md)
- [`product-service/`](product-service/README.md)
- [`cart-service/`](cart-service/README.md)
- [`wishlist-service/`](wishlist-service/README.md)
- [`payment-service/`](payment-service/README.md)

------------------

## Deployment

The Mankind Matrix AI Backend can be deployed on different cloud platforms. Choose the platform that best suits your needs:

### AWS Deployment
For deploying to Amazon Web Services (AWS), including:
- EC2 (Elastic Compute Cloud) instance setup
- VPC
- IAM User

[View AWS Deployment Guide →](docs/deploy/AWS-DEPLOY.md)

### Render Deployment
For deploying to Render.com, including:
- Web service setup
- Database configuration
- Environment variables management
- Automatic deployments

[View Render Deployment Guide →](docs/deploy/RENDER-DEPLOY.md)

> **Note:** Each deployment guide contains platform-specific instructions, cost considerations, and best practices. Choose the platform that best fits your requirements and budget.


------------------
## Project Structure

```
mankind-backend/
├── user-service/         # Handles user authentication & management
├── product-service/      # Handles product catalog functionality
├── cart-service/         # Handles shopping cart functionality
├── wishlist-service/     # Handles wishlist functionality
├── payment-service/      # Handles payment processing functionality
├── user-api/            # Shared DTOs and interfaces for user-service
├── product-api/         # Shared DTOs and interfaces for product-service
├── docs/                 # Documentation
│   └── deploy/          # Deployment guides
├── README.md            # Project overview and instructions
```

## Contributing

Create a branch for the feature you are working on. When you are done, create a Pull request and share it for review before merging.
