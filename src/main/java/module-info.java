module com.example.gerenciamentofarmacia {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gerenciamentofarmacia to javafx.fxml;
    exports com.example.gerenciamentofarmacia;
}