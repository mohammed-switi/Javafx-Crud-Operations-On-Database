module com.example.databaseassignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;


    opens com.example.databaseassignment to javafx.fxml;
    exports com.example.databaseassignment;
}