# Mankind Matrix AI Backend

This repository contains the backend microservices for the **Mankind Matrix AI** platform.

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

Choose your preferred method based on your needs. Docker is recommended for most users as it provides a consistent environment and simpler setup.

## Setup Guide

### Clone Repository

1. **Clone the Repository**
   ```bash
   git clone https://github.com/rebeccayilma/mankind-backend.git
   cd mankind-backend
   ```

2. **Verify the Clone**
   ```bash
   # List all services
   ls
   
   # Expected output should include:
   # user-service/
   # product-service/
   # cart-service/
   # wishlist-service/
   # docs/
   # README.md
   ```

### Database Setup

Before running the services, you need to have a MySQL database running. You have two options:

1. **Local Database**: Run MySQL on your machine
2. **External Database**: Use a remote MySQL database

For detailed database setup instructions, including:
- Database creation
- User setup
- Required tables and schemas
- Sample data

[View Database Setup Guide →](docs/database/README.MD)

After setting up the database, configure the connection in each service:

1. Copy the `.env.example` file from the root directory to each service directory:
   ```bash
   # From the root directory
   cp .env.example user-service/.env
   cp .env.example product-service/.env
   cp .env.example cart-service/.env
   cp .env.example wishlist-service/.env
   ```

2. Update the database connection variables in each service's `.env` file:
   - `DB_HOST`: Your database host (localhost or remote)
   - `DB_USERNAME`: Your database username
   - `DB_PASSWORD`: Your database password
   - Other variables can remain as they are in the example file

> **Note:** The `.env.example` file in the root directory contains all necessary configuration variables with sample values. You only need to update the database connection details in each service's `.env` file.

### Prerequisites

#### Local Development
- **Java**: JDK 17
- **MySQL**: 8.0 or newer
- **Maven**: For building the project

#### Docker Environment
- **Docker**: For containerization
- **Docker Compose**: For multi-container orchestration

> **Note:** You only need to install the prerequisites for your chosen method. If you're using Docker, you don't need to install Java, MySQL, or Maven locally.

### Installation

#### Local Development

##### Java (JDK 17)
<details>
<summary><b>Installation Instructions</b></summary>

###### macOS
```bash
# Install Homebrew (if not already installed)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Java
brew install openjdk@17

# Create Java symlink
sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

###### Windows
1. Download OpenJDK 17 from [Adoptium](https://adoptium.net/)
2. Run the installer
3. Configure environment: Set JAVA_HOME in System Environment Variables

###### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```
</details>

##### MySQL
<details>
<summary><b>Installation Instructions</b></summary>

###### macOS
```bash
brew install mysql
brew services start mysql
```

###### Windows
1. Download MySQL Installer from [MySQL Website](https://dev.mysql.com/downloads/installer/)
2. Choose "Server only" or "Custom" installation type
3. Follow the setup wizard

###### Linux (Ubuntu/Debian)
```bash
sudo apt install mysql-server
```
</details>

#### Docker Environment

##### Docker and Docker Compose
<details>
<summary><b>Installation Instructions</b></summary>

###### macOS
1. Install Docker Desktop for Mac
   - Download from [Docker's official website](https://www.docker.com/products/docker-desktop)
   - Docker Desktop includes both Docker Engine and Docker Compose
   - Follow the installation wizard

###### Windows
1. Install Docker Desktop for Windows
   - Download from [Docker's official website](https://www.docker.com/products/docker-desktop)
   - Ensure WSL 2 is installed (Docker Desktop will prompt if not)
   - Docker Desktop includes both Docker Engine and Docker Compose
   - Follow the installation wizard

###### Linux (Ubuntu/Debian)
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

## Services

Each service has its own detailed documentation. Click on the service name to view its specific README:

### Available Services
- [`user-service/`](user-service/README.md)
- [`product-service/`](product-service/README.md)
- [`cart-service/`](cart-service/README.md)
- [`wishlist-service/`](wishlist-service/README.md)

> **Note:** Each service's README contains specific setup instructions, API documentation, and additional details about that service.

## Running the Services

### Local Development

#### Prerequisites
- JDK 17
- MySQL 8.0+
- Maven

#### Steps

1. **Configure Environment**
   - Copy `.env.example` to `.env` in each microservice directory
   - Update the database connection details in each `.env` file
   - For detailed database configuration, see [Database Setup](#database-setup) section

2. **Build the Services**
   ```bash
   # Build all services at once
   mvn clean install
   
   # OR build individually
   cd user-service
   ./mvnw clean install
   
   cd ../product-service
   ./mvnw clean install
   ```

3. **Run Each Service**
   ```bash
   # Start user-service
   cd user-service
   ./mvnw spring-boot:run
   
   # In a new terminal, start product-service
   cd product-service
   ./mvnw spring-boot:run
   ```

**Service URLs (Local):**
- Product Service: `http://localhost:8080/swagger-ui/index.html`
- User Service: `http://localhost:8081/swagger-ui/index.html`
- Cart Service: `http://localhost:8082/swagger-ui/index.html`
- Wishlist Service: `http://localhost:8083/swagger-ui/index.html`

### Docker Environment

#### Prerequisites
- Docker
- Docker Compose

#### Steps

1. **Verify Docker Installation**
   ```bash
   # Check if Docker is running
   docker info
   ```

2. **Configure Environment**
   - Copy `.env.example` to `.env` in each microservice directory
   - Update the database connection details in each `.env` file
   - For detailed database configuration, see [Database Setup](#database-setup) section

3. **Run All Services**
   ```bash
   # Build and start all services
   docker-compose up --build

   ```

4. **Manage Docker Services**
   ```bash
   # Check running containers
   docker-compose ps
   
   # View logs for all services
   docker-compose logs
   
   # View logs for a specific service
   docker-compose logs product-service
   
   # Stop all services
   docker-compose down
   ```

**Service URLs (Docker):**
- Product Service: `http://localhost:8080/swagger-ui/index.html`
- User Service: `http://localhost:8081/swagger-ui/index.html`
- Cart Service: `http://localhost:8082/swagger-ui/index.html`
- Wishlist Service: `http://localhost:8083/swagger-ui/index.html`

**Note for Apple Silicon (M1/M2) Users:**
The Docker setup is configured to use `--platform=linux/amd64` for compatibility. Docker Desktop will handle the emulation automatically.

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

## Project Structure

```
mankind-backend/
├── user-service/         # Handles user authentication & management
├── product-service/      # Handles product catalog functionality
|--...                    # Other services
├── docs/                 # Documentation
│   └── deploy/          # Deployment guides
├── README.md            # Project overview and instructions
```

## Contributing

Create a branch for the feature you are working on and when done, create a Pull request and share it for review before merging.
