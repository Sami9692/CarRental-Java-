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

## OOP Design Principles Applied

| Principle        | Where Applied |
|------------------|---------------|
| Encapsulation    | All model classes use `private` fields + getters/setters |
| Abstraction      | All DAO operations defined as interfaces (`CarDAO`, `UserDAO`, etc.) |
| Inheritance      | Could extend `BaseDAOImpl` for shared JDBC boilerplate (extension point) |
| Single Responsibility | Each class has one job: model=data, DAO=queries, service=logic, controller=UI |
| Open/Closed      | New features add new DAO/Service; existing code unchanged |
| Dependency Inversion | Controllers depend on service abstractions, not DAO implementations directly |

## Design Patterns Used

| Pattern   | Implementation |
|-----------|----------------|
| Singleton  | `DBConnection` — one shared connection |
| DAO        | `CarDAO`, `UserDAO`, etc. — separate data access from business logic |
| MVC        | Model → DAO/Service, View → `MainMenu`/Controllers, Controller → `*Controller` |

## Cost Calculation Formula

```
totalCost = (car.dailyRate + insurance.dailyPrice) × numberOfDays
```
Implemented in `ReservationServiceImpl.calculateTotalCost()`.

## Overlap Detection Query

A new reservation `[newStart, newEnd)` conflicts with an existing one `[exStart, exEnd)` when:
```sql
existing.start_date < newEnd  AND  existing.end_date > newStart
```
Implemented in `ReservationDAOImpl.isCarAvailable()` using a `PreparedStatement`.

## Default Admin Account
After running `schema.sql`, register an admin via the console and manually set `is_admin = TRUE` in MySQL:
```sql
UPDATE User SET is_admin = TRUE WHERE email = 'youremail@example.com';
```
