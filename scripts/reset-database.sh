#!/bin/bash

echo "Step 1: Create Database"
mysql -uroot -p < "$(pwd)/src/main/java/data/create-database.sql"
if [ $? -eq 0 ]; then
  echo "Database created successfully!"
else
  echo "An error occurred while creating the database."
  exit 1
fi

echo "Step 2: Create Service Account"
mysql -uroot -p < "$(pwd)/src/main/java/data/create-service-account.sql"
if [ $? -eq 0 ]; then
  echo "Service account created successfully!"
else
  echo "An error occurred while creating the service account."
  exit 1
fi

echo "Step 3: Create Database Tables"
mysql delta_database -uroot -p < "$(pwd)/src/main/java/data/create-tables.sql"
if [ $? -eq 0 ]; then
  echo "Database tables created successfully!"
else
  echo "An error occurred while creating the database tables."
  exit 1
fi

echo "Step 4: Seeding Database"
mysql delta_database -uroot -p < "$(pwd)/src/main/java/data/seed-database.sql"
if [ $? -eq 0 ]; then
  echo "Database seeded successfully!"
else
  echo "An error occurred while seeding the database."
  exit 1
fi
