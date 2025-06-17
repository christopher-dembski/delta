echo "Step 1: Create Database"
mysql -uroot -p < "$(pwd)/src/main/java/data/create-database.sql"
if [ $? -eq 0 ]; then
  echo "Database created successfully!"
else
  echo "An error occurred while creating the database."
  exit 1
fi

echo "Step 2: Seed Database"
mysql -uroot -p < "$(pwd)/src/main/java/data/seed-database.sql"
if [ $? -eq 0 ]; then
  echo "Database seeded successfully!"
else
  echo "An error occurred while seeding the database."
  exit 1
fi
