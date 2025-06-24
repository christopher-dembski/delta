#!/bin/bash

echo "Waiting 30 seconds for MySQL to initialize before running tests..."
sleep 30

echo "Running tests"
exec mvn clean test
