# Mankind Matrix Product Service

A microservice for managing products, using MySQL as the database.

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


