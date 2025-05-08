# Mankind Matrix Product Service

A microservice for managing products, using MySQL as the database.

---

## Prerequisites Installation

### 1. Install Java (JDK 17)

#### For macOS:
```bash
# Install Homebrew (if not installed)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Java
brew install openjdk@17

# Link Java
sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

#### For Windows:
1. Download OpenJDK 17 from [Adoptium](https://adoptium.net/)
2. Run the installer
3. Set JAVA_HOME environment variable

#### For Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

### 2. Install MySQL

#### For macOS:
```bash
# Install MySQL
brew install mysql

# Start MySQL service
brew services start mysql

# Check if MySQL is running
brew services list | grep mysql

# Stop MySQL service
brew services stop mysql

# Restart MySQL service
brew services restart mysql

# Secure MySQL installation
mysql_secure_installation
```

#### For Windows:
1. Download MySQL Installer from [MySQL Official Website](https://dev.mysql.com/downloads/installer/)
2. Run the installer
3. Choose "Server only" or "Custom" installation
4. Follow the installation wizard
5. Remember to set a root password

#### For Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

### 3. Verify Installations
```bash
# Check Java version
java --version

# Check MySQL version
mysql --version
```

---

## Quick Setup

### 1. Clone the Project
```bash
git clone https://github.com/rebeccayilma/Mankind_Matrix_Product_Service.git
cd Mankind_Matrix_Product_Service
```

---

### 2. MySQL Setup

#### A. Start MySQL
Make sure MySQL is running on:
- **Host:** `localhost`
- **Port:** `3306`

#### Check MySQL Host and Port
```bash
# How to Check Port and Hostname
mysql -u root -p -e "SELECT @@hostname, @@port;"
```

#### B. Create Database and User
Paste the contents in `scripts/setup.sql` to MySQL Workbench or run from CLI:

---

### 3. Insert Sample Products (Optional)
Paste the contents of `scripts/sample_product_insert.sql` into MySQL Workbench or run from CLI.

---

### 4. Run the Application
```bash
./mvnw spring-boot:run
```

Or in IDE:
- Run `ProductServiceApplication.java`

---

### 6. Test the Endpoint
Use browser or  Postman to test:
```http
GET http://localhost:8080/api/v1/products/1
Accept: application/json
```
```http
GET http://localhost:8080/api/v1/products/category/{category}
Accept: application/json
```



