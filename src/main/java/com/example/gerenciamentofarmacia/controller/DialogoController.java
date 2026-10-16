package com.example.gerenciamentofarmacia.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/** Controlador do diálogo (dialogo-view.fxml): ícone, título e mensagem. */
public class DialogoController {

    @FXML private Label lblIcone;
    @FXML private Label lblTitulo;
    @FXML private Label lblMensagem;

    /** @param cor cor do ícone em CSS (ex.: #c0392b) */
    public void configurar(String icone, String cor, String titulo, String mensagem) {
        lblIcone.setText(icone);
        lblIcone.setStyle("-fx-background-color: " + cor + "; -fx-background-radius: 17;"
                + " -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        lblTitulo.setText(titulo);
        lblMensagem.setText(mensagem);
    }
}
