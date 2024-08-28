package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;

@SuppressWarnings("unused")
public class TelainicialController {
    @FXML
    private Button btnuniversos;
    private Stage stage = new Stage();
    public WavPlayer player = new WavPlayer();

    public TelainicialController() {

    }

    public TelainicialController(Stage arg8) {
        stage = arg8;
        arg8.initStyle(StageStyle.UNDECORATED);
    }

    @FXML
    public void btnuniversosClickAction(ActionEvent event) {
        try {
            @SuppressWarnings("unused")
            UniversosController universoscontroller = new UniversosController(stage);
            // Carrega o novo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/universos.fxml"));
            Parent root = loader.load();

            stage.setTitle("Universos");
            stage.setScene(new Scene(root));
            stage.show();
            Stage stagePrincipal = (Stage) btnuniversos.getScene().getWindow();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Iniciar() {
        this.stage.show();
    }

    public void btnconfiguracoesClickAction(ActionEvent event) {
        try {
            @SuppressWarnings("unused")

            ConfiguracoesController configuracoescontroller = new ConfiguracoesController(stage);
            // Carrega o novo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/configuracoes.fxml"));
            Parent root = loader.load();

            stage.setTitle("configuracoes");
            stage.setScene(new Scene(root));
            stage.show();
            @SuppressWarnings("unused")
            Stage stagePrincipal = (Stage) btnuniversos.getScene().getWindow();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stageStyle() {
        stage.initStyle(StageStyle.UNDECORATED);
    }

    public void btnsairClickAction(ActionEvent event) {
        Platform.exit();
    }
}
