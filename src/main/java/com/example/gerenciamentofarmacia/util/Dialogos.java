package com.example.gerenciamentofarmacia.util;

import com.example.gerenciamentofarmacia.controller.DialogoController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.io.UncheckedIOException;

/** Mostra os diálogos de erro e confirmação carregando o dialogo-view.fxml. */
public final class Dialogos {

    private Dialogos() {}

    public static void erro(String titulo, String mensagem) {
        abrir("✕", "#c0392b", "Erro", titulo, mensagem, ButtonType.OK);
    }

    /** @return true se o usuário clicou em "Sim" */
    public static boolean confirmar(String titulo, String mensagem) {
        return abrir("?", "#0096c9", "Confirmação", titulo, mensagem, ButtonType.YES, ButtonType.NO) == ButtonType.YES;
    }

    private static ButtonType abrir(String icone, String cor, String tituloJanela,
                                    String titulo, String mensagem, ButtonType... botoes) {
        try {
            FXMLLoader loader = new FXMLLoader(Dialogos.class.getResource("dialogo-view.fxml"));
            DialogPane painel = loader.load();
            DialogoController controller = loader.getController();
            controller.configurar(icone, cor, titulo, mensagem);
            painel.getButtonTypes().setAll(botoes);

            Dialog<ButtonType> dialogo = new Dialog<>();
            dialogo.setTitle(tituloJanela);
            dialogo.setDialogPane(painel);
            return dialogo.showAndWait().orElse(ButtonType.CANCEL);
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível carregar dialogo-view.fxml", e);
        }
    }
}
