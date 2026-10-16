module com.example.gerenciamentofarmacia {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gerenciamentofarmacia to javafx.fxml;
    exports com.example.gerenciamentofarmacia;
    exports com.example.gerenciamentofarmacia.repository;
    opens com.example.gerenciamentofarmacia.repository to javafx.fxml;
    exports com.example.gerenciamentofarmacia.service;
    opens com.example.gerenciamentofarmacia.service to javafx.fxml;
    exports com.example.gerenciamentofarmacia.validation;
    opens com.example.gerenciamentofarmacia.validation to javafx.fxml;
    exports com.example.gerenciamentofarmacia.controller;
    opens com.example.gerenciamentofarmacia.controller to javafx.fxml;
    exports com.example.gerenciamentofarmacia.util;
    opens com.example.gerenciamentofarmacia.util to javafx.fxml;
    exports com.example.gerenciamentofarmacia.model;
    opens com.example.gerenciamentofarmacia.model to javafx.fxml;
}
