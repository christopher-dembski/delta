#!/bin/bash

echo "Step 1: Create Development Database"
mysql -uroot -p < "$(pwd)/src/main/java/data/create-database.sql"
if [ $? -eq 0 ]; then
  echo "Development database created successfully!"
else
  echo "An error occurred while creating the development database."
  exit 1
fi

echo "Step 2: Create Test Database"
mysql -uroot -p < "$(pwd)/src/main/java/data/create-test-database.sql"
if [ $? -eq 0 ]; then
  echo "Test database created successfully!"
else
  echo "An error occurred while creating the test database."
  exit 1
fi

echo "Step 3: Create Service Account"
mysql -uroot -p < "$(pwd)/src/main/java/data/create-service-account.sql"
if [ $? -eq 0 ]; then
  echo "Service account created successfully!"
else
  echo "An error occurred while creating the service account."
  exit 1
fi

echo "Step 4: Create Development Database Tables"
mysql delta_database -uroot -p < "$(pwd)/src/main/java/data/create-tables.sql"
if [ $? -eq 0 ]; then
  echo "Development database tables created successfully!"
else
  echo "An error occurred while creating the development database tables."
  exit 1
fi

echo "Step 5: Seeding Development Database"
mysql delta_database -uroot -p < "$(pwd)/src/main/java/data/seed-database.sql"
if [ $? -eq 0 ]; then
  echo "Development database seeded successfully!"
else
  echo "An error occurred while seeding the database."
  exit 1
fi

echo "Step 6: Create Test Database Tables"
mysql delta_test_database -uroot -p < "$(pwd)/src/main/java/data/create-tables.sql"
if [ $? -eq 0 ]; then
  echo "Test database tables created successfully!"
else
  echo "An error occurred while creating the test database tables."
  exit 1
fi

echo "Step 7: Seeding Test Database"
mysql delta_test_database -uroot -p < "$(pwd)/src/main/java/data/seed-database.sql"
if [ $? -eq 0 ]; then
  echo "Test database seeded successfully!"
else
  echo "An error occurred while seeding the test database."
  exit 1
fi
