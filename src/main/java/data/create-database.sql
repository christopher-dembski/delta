DROP DATABASE IF EXISTS delta_database;
CREATE DATABASE delta_database;

USE delta_database;

-- example table for testing
CREATE TABLE students
(
    id   int PRIMARY KEY AUTO_INCREMENT,
    name varchar(255)
);
