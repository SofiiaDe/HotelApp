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

CREATE TABLE applications
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
