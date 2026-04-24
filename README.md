# Online Car Rental System — Java + JDBC + MVC

## Project Structure

```
CarRentalSystem/
├── schema.sql                          ← MySQL database schema + seed data
├── README.md
└── src/main/java/com/carrental/
    ├── util/
    │   ├── DBConnection.java           ← Singleton DB connection
    │   └── PasswordUtil.java           ← SHA-256 password hashing
    ├── model/                          ← POJOs (one per table)
    │   ├── Car.java
    │   ├── CarType.java
    │   ├── RentalLocation.java
    │   ├── InsuranceType.java
    │   ├── InsurancePrice.java
    │   ├── User.java
    │   ├── UserCredential.java
    │   ├── CardDetails.java
    │   ├── Reservation.java
    │   ├── Payment.java
    │   └── UserRentsCar.java
    ├── dao/                            ← DAO interfaces (abstraction)
    │   ├── CarDAO.java
    │   ├── UserDAO.java
    │   ├── ReservationDAO.java
    │   ├── PaymentDAO.java
    │   ├── CardDetailsDAO.java
    │   └── InsuranceDAO.java
    ├── dao/impl/                       ← JDBC implementations
    │   ├── CarDAOImpl.java
    │   ├── UserDAOImpl.java
    │   ├── ReservationDAOImpl.java
    │   ├── PaymentDAOImpl.java
    │   ├── CardDetailsDAOImpl.java
    │   └── InsuranceDAOImpl.java
    ├── service/impl/                   ← Business logic layer
    │   ├── AuthServiceImpl.java
    │   ├── CarServiceImpl.java
    │   ├── ReservationServiceImpl.java
    │   └── PaymentServiceImpl.java
    ├── controller/                     ← MVC controllers (user interaction)
    │   ├── AuthController.java
    │   ├── CarController.java
    │   ├── ReservationController.java
    │   └── PaymentController.java
    └── ui/
        └── MainMenu.java               ← Entry point (console menu)
```

## Setup & Run

### 1. Prerequisites
- **Java 17+** (JDK)
- **MySQL 8.x**
- **Node.js** (for the frontend)
- **Maven** (optional, but recommended for dependency management)

### 2. Database Setup
1. Open your MySQL terminal.
2. Run the schema script to create the database and seed data:
```bash
mysql -u root -p < schema.sql
```

### 3. Backend Setup (Java API)
1. **Configure DB credentials**: 
   Edit `src/main/java/com/carrental/util/DBConnection.java` with your MySQL username and password.
2. **Run with Maven**:
   From the root directory:
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.carrental.api.ApiMain"
   ```
   *The API will start on `http://localhost:8080`.*

### 4. Frontend Setup (React + Vite)
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the development server:
   ```bash
   npm run dev
   ```
   *The frontend will be available at the URL shown in your terminal (usually `http://localhost:5173`).*

## Project Structure

```
CarRentalSystem/
├── schema.sql                          ← MySQL database schema + seed data
├── pom.xml                             ← Maven dependencies
├── frontend/                           ← React + Vite frontend application
└── src/main/java/com/carrental/
    ├── api/                            ← REST API Handlers (Javalin/HttpServer)
    ├── service/                        ← Business logic
    ├── dao/                            ← Data Access Objects (JDBC)
    └── model/                          ← POJOs / Entities
```

