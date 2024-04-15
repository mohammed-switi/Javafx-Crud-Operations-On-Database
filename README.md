

# JavaFX Car Database CRUD Application

This JavaFX application is designed to perform CRUD (Create, Read, Update, Delete) operations on a car database using Java.

## Features

- **Create:** Add new cars to the database with details such as make, model, year, and price.
- **Read:** View the list of cars currently stored in the database.
- **Update:** Modify existing car details such as price or year.
- **Delete:** Remove cars from the database.

## Technologies Used

- Java
- JavaFX
- JDBC (Java Database Connectivity)

## Getting Started

To run this application locally, follow these steps:

1. Clone this repository to your local machine:

   ```bash
   git clone https://github.com/mohammed-switi/Javafx-Crud-Operations-On-Database.git
   ```

2. Open the project in your favorite Java IDE (e.g., IntelliJ IDEA, Eclipse).

3. Configure the database connection details in the application. You may need to modify the JDBC URL, username, and password in the source code to match your database setup.

4. Build and run the application.

## Database Schema

The database schema for this project includes a single table named `cars`, which stores information about each car:

| Column    | Data Type |
| --------- | --------- |
| id        | INT       |
| make      | VARCHAR   |
| model     | VARCHAR   |
| year      | INT       |
| price     | DECIMAL   |


