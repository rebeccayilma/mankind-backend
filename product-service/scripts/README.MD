# Database Setup

This guide provides instructions for setting up and configuring the database connection for the application.

## Overview
These steps ensure successful database connectivity for the application.

## Setup Steps

### 1. Database Host Configuration
- Set up a database host (local or remote)
- For local databases, verify MySQL service status:
  ```bash
  # macOS
  brew services list | grep mysql
  
  # Linux
  systemctl status mysql
  ```

### 2. Database Connection Testing
1. Ensure MySQL is running and update the `.env` file with your database connection details
2. Run the connection test:
   ```bash
   ./mvnw test -Dtest=ConnectionTest
   ```
3. Optional: Install MySQL Workbench
   - Download from [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)
   - Use it to connect and manage your database

### 3. Database Schema Creation
Run the following command to create the database schema:
```bash
mysql -h <HOST> -u <USER> -p'<PASSWORD>' < scripts/setup.sql
```

Example for remote environment:
```bash
mysql -h mankind-matrix-db.cd0qkick6gy2.us-west-1.rds.amazonaws.com -u matrix_user -p'matrix_pass' < scripts/setup.sql
```

### 4. Table Creation
The application uses automatic table mapping, which generates or updates tables during runtime.

To start the application:
```bash
./mvnw spring-boot:run
```

To verify created tables:
```bash
mysql -h <HOST> -u <USER> -p'<PASSWORD>' -e "SHOW DATABASES;"
```

Example for remote environment:
```bash
mysql -h mankind-matrix-db.cd0qkick6gy2.us-west-1.rds.amazonaws.com -u matrix_user -p'matrix_pass' -e "SHOW DATABASES;"
```

### 5. Sample Data Insertion
To populate the database with sample data:
```bash
mysql -h <HOST> -u <USER> -p'<PASSWORD>' < scripts/sample_product_insert.sql
```

Example for remote environment:
```bash
mysql -h mankind-matrix-db.cd0qkick6gy2.us-west-1.rds.amazonaws.com -u matrix_user -p'matrix_pass' < scripts/sample_product_insert.sql
```
