DROP DATABASE IF EXISTS delta_database;
CREATE DATABASE delta_database;

USE delta_database;

-- example table for testing
CREATE TABLE students
(
    id   int PRIMARY KEY AUTO_INCREMENT,
    name varchar(255)
);

CREATE TABLE profiles(
     id            VARCHAR(36) PRIMARY KEY,
     full_name     VARCHAR(80) NOT NULL,
     age           INT         NOT NULL,
     sex           VARCHAR(10) NOT NULL,
     dob           DATE        NOT NULL,
     height     DOUBLE      NOT NULL,
     weight     DOUBLE      NOT NULL,
     unit_system   VARCHAR(10) NOT NULL
);