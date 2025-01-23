module com.posone {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.posone to javafx.fxml;
    exports com.posone;
    exports com.posone.model;
    opens com.posone.model to javafx.fxml;
    exports com.posone.dao;
    opens com.posone.dao to javafx.fxml;
}