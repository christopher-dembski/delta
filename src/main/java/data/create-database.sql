DROP DATABASE IF EXISTS delta_database;
CREATE DATABASE delta_database;

USE delta_database;

-- example table for testing
CREATE TABLE students
(
    id   int PRIMARY KEY AUTO_INCREMENT,
    name varchar(255)
);

-- Additional tables for CSV import demo
-- Professors table (create first for foreign key reference)
CREATE TABLE professors
(
    prof_id    int PRIMARY KEY AUTO_INCREMENT,
    first_name varchar(100) NOT NULL,
    last_name  varchar(100) NOT NULL,
    department varchar(100)
);

-- Courses table (enhanced with professor relationship)
CREATE TABLE courses
(
    course_id   varchar(10) PRIMARY KEY,
    course_name varchar(255) NOT NULL,
    credits     int NOT NULL,
    department  varchar(100),
    professor_id int,
    FOREIGN KEY (professor_id) REFERENCES professors(prof_id)
);

-- Grades table
CREATE TABLE grades
(
    grade_id   int PRIMARY KEY AUTO_INCREMENT,
    student_id int,
    course_id  varchar(10),
    grade      varchar(2),
    semester   varchar(20)
);
