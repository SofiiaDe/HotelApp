CREATE TABLE users
(
    id      INT PRIMARY KEY auto_increment,
    firstName    VARCHAR(20),
    lastName VARCHAR(20),
    email   VARCHAR(50) UNIQUE,
    password VARCHAR(20),
    country VARCHAR(20),
    role VARCHAR(13)
);