-- ============================================================
--  Online Car Rental System – MySQL Schema
--  Compatible with the Java JDBC project
-- ============================================================

CREATE DATABASE IF NOT EXISTS car_rental_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE car_rental_db;

-- ── Location ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Rental_Location (
    location_id  INT AUTO_INCREMENT PRIMARY KEY,
    city         VARCHAR(100) NOT NULL,
    state        VARCHAR(100),
    country      VARCHAR(100) NOT NULL,
    address      VARCHAR(255)
);

-- ── Car Type ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Car_Type (
    type_id     INT AUTO_INCREMENT PRIMARY KEY,
    type_name   VARCHAR(100) NOT NULL,
    description TEXT
);

-- ── Car ──────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Car (
    car_id        INT AUTO_INCREMENT PRIMARY KEY,
    make          VARCHAR(100) NOT NULL,
    model         VARCHAR(100) NOT NULL,
    year          YEAR         NOT NULL,
    license_plate VARCHAR(20)  NOT NULL UNIQUE,
    status        ENUM('available','rented','maintenance') DEFAULT 'available',
    daily_rate    DECIMAL(10,2) NOT NULL,
    type_id       INT,
    location_id   INT,
    image_url     VARCHAR(255),
    FOREIGN KEY (type_id)     REFERENCES Car_Type(type_id),
    FOREIGN KEY (location_id) REFERENCES Rental_Location(location_id)
);

-- ── Insurance ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Insurance_Type (
    insurance_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name         VARCHAR(100) NOT NULL,
    coverage          TEXT
);

CREATE TABLE IF NOT EXISTS Insurance_Price (
    price_id          INT AUTO_INCREMENT PRIMARY KEY,
    insurance_type_id INT NOT NULL,
    daily_price       DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (insurance_type_id) REFERENCES Insurance_Type(insurance_type_id)
);

-- ── User ─────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS User (
    user_id        INT AUTO_INCREMENT PRIMARY KEY,
    first_name     VARCHAR(100) NOT NULL,
    last_name      VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL UNIQUE,
    date_of_birth  DATE,
    license_number VARCHAR(50),
    is_admin       BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS User_Phone (
    phone_id   INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT NOT NULL,
    phone      VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS User_Credential (
    credential_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT NOT NULL UNIQUE,
    username      VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- ── Payment ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Card_Details (
    card_id     INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    card_number VARCHAR(4)   NOT NULL,   -- last 4 digits only
    card_holder VARCHAR(150) NOT NULL,
    expiry_date VARCHAR(7)   NOT NULL,   -- MM/YY
    card_type   VARCHAR(50)  NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- ── Reservation ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Reservation (
    reservation_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_id           INT NOT NULL,
    car_id            INT NOT NULL,
    location_id       INT,
    start_date        DATE NOT NULL,
    end_date          DATE NOT NULL,
    status            ENUM('pending','confirmed','cancelled','completed') DEFAULT 'pending',
    insurance_type_id INT,
    FOREIGN KEY (user_id)           REFERENCES User(user_id),
    FOREIGN KEY (car_id)            REFERENCES Car(car_id),
    FOREIGN KEY (location_id)       REFERENCES Rental_Location(location_id),
    FOREIGN KEY (insurance_type_id) REFERENCES Insurance_Type(insurance_type_id)
);

CREATE TABLE IF NOT EXISTS Payment (
    payment_id     INT AUTO_INCREMENT PRIMARY KEY,
    reservation_id INT NOT NULL,
    card_id        INT,
    amount         DECIMAL(10,2) NOT NULL,
    payment_date   DATETIME      DEFAULT NOW(),
    payment_status ENUM('pending','completed','refunded') DEFAULT 'pending',
    payment_method VARCHAR(50),
    FOREIGN KEY (reservation_id) REFERENCES Reservation(reservation_id),
    FOREIGN KEY (card_id)        REFERENCES Card_Details(card_id)
);

-- ── Rental Log ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS User_Rents_Car (
    rental_id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id            INT NOT NULL,
    car_id             INT NOT NULL,
    reservation_id     INT NOT NULL,
    actual_pickup_date DATE,
    actual_return_date DATE,
    rental_status      ENUM('active','completed') DEFAULT 'active',
    FOREIGN KEY (user_id)        REFERENCES User(user_id),
    FOREIGN KEY (car_id)         REFERENCES Car(car_id),
    FOREIGN KEY (reservation_id) REFERENCES Reservation(reservation_id)
);

-- ── Seed Data ────────────────────────────────────────────────
INSERT IGNORE INTO Rental_Location (city, state, country, address)
VALUES ('New York','NY','USA','123 Broadway'),
       ('Los Angeles','CA','USA','456 Sunset Blvd'),
       ('Chennai','TN','India','789 Anna Salai');

INSERT IGNORE INTO Car_Type (type_name, description)
VALUES ('Sedan','Comfortable 4-door sedan'),
       ('SUV','Spacious sport utility vehicle'),
       ('Hatchback','Compact hatchback'),
       ('Luxury','Premium luxury car');

INSERT IGNORE INTO Insurance_Type (type_name, coverage)
VALUES ('Basic','Covers third-party liability only'),
       ('Standard','Third-party + collision'),
       ('Premium','Full comprehensive coverage');

INSERT IGNORE INTO Insurance_Price (insurance_type_id, daily_price)
VALUES (1, 5.00), (2, 12.00), (3, 25.00);

INSERT IGNORE INTO Car (make, model, year, license_plate, status, daily_rate, type_id, location_id, image_url)
VALUES ('Toyota','Camry',  2022,'ABC-1234','available',45.00,1,1, 'https://images.unsplash.com/photo-1621007947382-bb3c3994e3fd?auto=format&fit=crop&w=800&q=80'),
       ('Honda', 'CR-V',   2023,'DEF-5678','available',65.00,2,1, 'https://images.unsplash.com/photo-1568844293986-8d0400bc4745?auto=format&fit=crop&w=800&q=80'),
       ('Ford',  'Focus',  2021,'GHI-9012','available',38.00,3,2, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=800&q=80'),
       ('BMW',   '5 Series',2023,'JKL-3456','available',120.00,4,2, 'https://images.unsplash.com/photo-1555215695-3004980ad54e?auto=format&fit=crop&w=800&q=80'),
       ('Maruti','Swift',  2022,'MNO-7890','available',30.00,3,3, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80');

-- Admin user (password: admin123)
INSERT IGNORE INTO User (first_name,last_name,email,license_number,is_admin)
VALUES ('Admin','User','admin@carrental.com','ADMIN-LIC',TRUE);

-- Password hash for "admin123"
INSERT IGNORE INTO User_Credential (user_id,username,password_hash)
SELECT user_id,'admin','v2USeqcxsUH5owJb5n17IA==$YT7E1QweUYYt7QxZIVGykm0M3vZKO24y+M2HG79pz2Y='
FROM User WHERE email='admin@carrental.com';

