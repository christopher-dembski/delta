-- the service account is the user that represents the app
DROP USER IF EXISTS 'delta-service-account'@'localhost';
CREATE USER 'delta-service-account'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON delta_database.* TO 'delta-service-account'@'localhost';
GRANT ALL PRIVILEGES ON delta_test_database.* TO 'delta-service-account'@'localhost';
