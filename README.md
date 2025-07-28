# Delta - Nutrition Tracking Application
Course project for EECS-3311 (Software Design) at York University.

A comprehensive Java Swing desktop application for nutrition tracking, meal logging, and food swap recommendations built with clean MVP architecture and extensive design pattern implementation.

> [!WARNING]
> This software was developed as part of an academic project focused on software design principles. It is not intended for production use and may contain known errors and security risks.

## Table of Contents
- [Features](#features)
- [System Requirements](#system-requirements)
- [Setup Instructions](#setup-instructions)
- [Running the Application](#running-the-application)
- [Module Overview](#module-overview)
- [Architecture](#architecture)
- [Design Patterns](#design-patterns)
- [Testing](#testing)

## Features
- **User Profile Management**: Create and manage multiple user profiles with personal nutrition information
- **Meal Logging**: Log meals with detailed food items, quantities, and nutritional breakdown
- **Nutrition Analysis**: View comprehensive nutrient breakdowns with interactive charts
- **Food Swaps**: Get intelligent food swap recommendations for healthier alternatives
- **Statistics Dashboard**: Analyze nutrition trends and meal patterns over time
- **CSV Data Import**: Import USDA nutrition database for comprehensive food information

## System Requirements
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (local installation or Docker)
- Docker and Docker Compose (optional - for containerized database)

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/christopher-dembski/delta.git
cd delta
```

### 2. Database Setup

#### Option A: Using Docker (Recommended for Development)
Start the MySQL server and initialize the database:
```bash
docker compose up -d
./scripts/reset-database.sh
```

#### Option B: Using Local MySQL
If you have MySQL running locally on your machine:
```bash
./scripts/reset-database.sh
```

> [!NOTE]
> The reset script will automatically detect your MySQL setup and configure the database accordingly. No additional Docker setup is required if you're using a local MySQL installation.

### 3. Install Dependencies
```bash
mvn clean compile
```

## Running the Application

### Start the Application
```bash
mvn clean compile exec:java -Dexec.mainClass="app.AppMainPresenter" -e
```

### Run Tests
```bash
mvn test
```

### Run CI Tests (Headless Mode)
```bash
mvn clean test -B -Djava.awt.headless=true
```

## Module Overview

### Profile Module (`profile/`)
- **Purpose**: User sign-up & profile management
- **Key Components**: 
  - `ProfileService`: User creation, validation, session management
  - `UserRepository`: Data persistence for user profiles
  - Builder pattern implementation for Profile creation
- **Views**: Profile selection, creation, and editing forms

### Meals Module (`meals/`)
- **Purpose**: Meal logging and food item management
- **Key Components**:
  - `MealService`: Meal creation and querying
  - `QueryFoodsService`: Food database access
  - MVP pattern throughout all meal-related views
- **Views**: Meal logging form, meal list, detailed meal view

### Swaps Module (`swaps/`)
- **Purpose**: Intelligent food swap recommendations
- **Key Components**:
  - `SwapGenerationService`: Algorithm for finding optimal food substitutions
  - Multi-step workflow with goal setting and swap selection
  - Integration with meal data for personalized recommendations
- **Views**: Goal definition, swap selection, meal comparison

### Statistics Module (`statistics/`)
- **Purpose**: Nutrition analysis and data visualization
- **Key Components**:
  - `StatisticsService`: Nutrient aggregation and analysis
  - JFreeChart integration for interactive visualizations
  - Date range selection and filtering capabilities
- **Views**: Nutrient breakdown charts, food guide alignment

### Shared Module (`shared/`)
- **Purpose**: Common infrastructure and utilities
- **Key Components**:
  - `ServiceFactory`: Dependency injection and service management
  - `AppBackend`: Global application configuration
  - Navigation framework and reusable UI components
  - Common utilities for date handling and service outputs

### Data Module (`data/`)
- **Purpose**: Database abstraction and query building
- **Key Components**:
  - Custom query builders (`SelectQuery`, `InsertQuery`, `UpdateQuery`)
  - MySQL driver implementation with connection management
  - Database configuration and migration scripts

### CSV Module (`csv/`)
- **Purpose**: USDA nutrition data import capabilities
- **Key Components**:
  - `CSVImportService`: Parse and import nutrition databases
  - `CSVRecordAdapter`: Adapter pattern for data transformation
  - Nutrition data loading and validation

## Architecture

### 5-Layer Clean Architecture
1. **View Layer**: Java Swing UI components with proper separation of concerns
2. **Presenter Layer**: MVP pattern implementation for business logic
3. **Service Layer**: Singleton services with standardized error handling
4. **Data Access Layer**: Repository pattern with custom query builders
5. **Database Layer**: MySQL integration with Docker containerization


## Design Patterns

The application demonstrates extensive use of design patterns:

### Creational Patterns
- **Singleton**: All service classes (`QueryMealsService`, `StatisticsService`, etc.)
- **Builder**: Profile creation with fluent interface and validation
- **Factory**: `ServiceFactory` for dependency injection

### Structural Patterns  
- **Adapter**: `CSVRecordAdapter` for data transformation
- **Facade**: `AppMainPresenter` simplifies complex subsystem interactions

### Behavioral Patterns
- **Template Method**: Database operations with customizable steps
- **Observer**: Event-driven navigation and UI updates
- **State**: Multi-step workflows in swaps module

## Testing

### Unit Tests
```bash
mvn clean test -B -Djava.awt.headless=true
```

### Test Structure
- **Profile Module**: Profile creation, validation, and session management
- **Meals Module**: Meal logging, food querying, and data persistence  
- **Data Layer**: Query builders, database operations, and repository patterns


