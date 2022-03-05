CREATE TABLE users
(
    id      INT PRIMARY KEY auto_increment,
    first_name    VARCHAR(20),
    last_name VARCHAR(20),
    email   VARCHAR(50) UNIQUE,
    password VARCHAR(100),
    country VARCHAR(20),
    role VARCHAR(13)
);

CREATE TABLE claims
(
    id            INT PRIMARY KEY auto_increment,
    user_id INT,
    room_seats    ENUM('single', 'double', 'triple', 'twin'),
    room_class    ENUM('standard', 'business', 'lux'),
    checkin_date  DATE,
    checkout_date DATE,
    CONSTRAINT FK_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE confirmation_requests
(
    id            INT PRIMARY KEY auto_increment,
    user_id        INT,
    application_id INT,
    room_id INT,
    confirm_request_date DATE,
    status ENUM('new', 'confirmed'),
    CONSTRAINT FK_2 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE

);

CREATE TABLE bookings
(
    id          INT PRIMARY KEY auto_increment,
    user_id        INT,
    checkin_date  DATE,
    checkout_date DATE,
    room_id INT,
    application_id INT,
    CONSTRAINT FK_3 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE invoices
(
    id          INT PRIMARY KEY auto_increment,
    user_id        INT,
    amount DECIMAL(10, 2) NOT NULL,
    booking_id INT,
    invoice_date DATE,
    status ENUM('new', 'paid', 'cancelled')

);

CREATE TABLE rooms
(
    id          INT PRIMARY KEY auto_increment,
    price       DECIMAL(10, 2) NOT NULL,
    room_number INT UNIQUE,
    room_seats  ENUM ('single', 'double', 'triple', 'twin'),
    room_class  ENUM ('standard', 'business', 'lux'),
    room_status ENUM ('available', 'unavailable')

);

INSERT INTO rooms (id, price, room_number, room_seats, room_class, room_status)
VALUES (DEFAULT, 200, 31, 'single', 'business', 'available'),
       (DEFAULT, 150, 32, 'double', 'standard', 'available'),
       (DEFAULT, 250, 33, 'twin', 'business', 'unavailable'),
       (DEFAULT, 250, 34, 'triple', 'standard', 'available'),
       (DEFAULT, 350, 35, 'double', 'lux', 'available'),
       (DEFAULT, 200, 36, 'single', 'business', 'available'),
       (DEFAULT, 150, 37, 'double', 'standard', 'available'),
       (DEFAULT, 250, 38, 'double', 'business', 'available'),
       (DEFAULT, 300, 39, 'triple', 'business', 'unavailable'),
       (DEFAULT, 350, 40, 'twin', 'lux', 'available'),
       (DEFAULT, 300, 41, 'single', 'lux', 'available'),
       (DEFAULT, 150, 42, 'double', 'standard', 'available'),
       (DEFAULT, 250, 43, 'double', 'business', 'available'),
       (DEFAULT, 300, 44, 'triple', 'business', 'available'),
       (DEFAULT, 200, 45, 'twin', 'standard', 'available'),
       (DEFAULT, 150, 46, 'single', 'standard', 'available');