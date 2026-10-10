package com.example.gerenciamentofarmacia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FarmaciaApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FarmaciaApplication.class.getResource("farmacia-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 680);
        stage.setTitle("Estoque de Medicamentos - Farmácia");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}