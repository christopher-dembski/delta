DROP DATABASE IF EXISTS delta_database;
CREATE DATABASE delta_database;

USE delta_database;

-- Example tables for testing

CREATE TABLE students
(
    id   int PRIMARY KEY AUTO_INCREMENT,
    name varchar(255)
);

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

-- ======================================
-- NUTRITION TABLES
-- ======================================

-- Food Groups table (independent entity) - French fields nullable (ignored by Java)
CREATE TABLE food_groups
(
    FoodGroupID   int PRIMARY KEY,
    FoodGroupCode int,
    FoodGroupName varchar(255),
    FoodGroupNameF varchar(255) DEFAULT NULL  -- Nullable - Java ignores this
);

-- Nutrients table (independent entity) - French fields nullable (ignored by Java)
CREATE TABLE nutrients
(
    NutrientID       int PRIMARY KEY,
    NutrientCode     int,
    NutrientSymbol   varchar(20),
    NutrientUnit     varchar(20),
    NutrientName     varchar(255),
    NutrientNameF    varchar(255) DEFAULT NULL,  -- Nullable - Java ignores this
    Tagname          varchar(100),
    NutrientDecimals int
);

-- Foods table (has foreign key to food_groups) - French fields nullable (ignored by Java)
CREATE TABLE foods
(
    FoodID                 int PRIMARY KEY,
    FoodCode               int,
    FoodGroupID            int,
    FoodSourceID           int,
    FoodDescription        varchar(500),
    FoodDescriptionF       varchar(500) DEFAULT NULL,  -- Nullable - Java ignores this
    FoodDateOfEntry        varchar(20) DEFAULT NULL,   -- Nullable - Java ignores this  
    FoodDateOfPublication  varchar(20) DEFAULT NULL,   -- Nullable - Java ignores this
    CountryCode            varchar(10),
    ScientificName         varchar(255),
    
    FOREIGN KEY (FoodGroupID) REFERENCES food_groups(FoodGroupID)
);

-- Nutrient Amounts table (junction table - nutritional values per food)
CREATE TABLE nutrient_amounts
(
    FoodID                int,
    NutrientID           int,
    NutrientValue        double,
    NutrientSourceID     int,
    NutrientDateOfEntry  varchar(20) DEFAULT NULL, -- Nullable - Java ignores this
    
    PRIMARY KEY (FoodID, NutrientID),
    FOREIGN KEY (FoodID) REFERENCES foods(FoodID),
    FOREIGN KEY (NutrientID) REFERENCES nutrients(NutrientID)
);
