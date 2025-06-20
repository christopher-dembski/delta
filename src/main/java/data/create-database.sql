-- drop/create database
DROP DATABASE IF EXISTS delta_database;
CREATE DATABASE delta_database;

-- drop service account user and create new user for service account
-- the service account is the user that represents the app
DROP USER IF EXISTS 'delta-service-account'@'localhost';
CREATE USER 'delta-service-account'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'delta-service-account'@'localhost';

-- execute SQL statements on this database
USE delta_database;

-- create tables

-- example tables for testing
CREATE TABLE students
(
    id   int PRIMARY KEY AUTO_INCREMENT,
    name varchar(255)
);
