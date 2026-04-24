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
- Java 17+ (uses switch expressions, `var` optional)
- MySQL 8.x
- MySQL Connector/J JAR (`mysql-connector-j-8.x.x.jar`)

### 2. Database Setup
```bash
mysql -u root -p < schema.sql
```

### 3. Configure DB credentials
Edit `src/main/java/com/carrental/util/DBConnection.java`:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/car_rental_db?...";
private static final String USER     = "root";
private static final String PASSWORD = "your_password_here";
```

### 4. Compile
```bash
# From project root
mkdir -p out
javac -cp "lib/mysql-connector-j-8.x.x.jar" \
      -d out \
      $(find src -name "*.java")
```

### 5. Run
```bash
java -cp "out:lib/mysql-connector-j-8.x.x.jar" com.carrental.ui.MainMenu
```
> On Windows use `;` instead of `:` in classpath.

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
