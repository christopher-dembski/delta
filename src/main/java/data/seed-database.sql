USE delta_database;

-- seed example table for testing
INSERT INTO students
VALUES (1, 'Chris');
INSERT INTO students
VALUES (2, 'Nam');
INSERT INTO students
VALUES (3, 'Jae');
INSERT INTO students
VALUES (4, 'Abdullah');

-- seed profiles table with 1 test user
INSERT INTO profiles (full_name, age, sex, dob, height, weight, unit_system)
VALUES 
    ('John Doe', 25, 'MALE', '1999-07-22', 175.0, 70.0, 'METRIC'),

