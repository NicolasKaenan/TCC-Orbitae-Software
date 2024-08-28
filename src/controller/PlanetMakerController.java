package controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PlanetMakerController {
    private Stage stage = new Stage();
    @FXML
    Button btnvoltar;
    public PlanetMakerController() {}

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void btncriarClickAction(ActionEvent event) {}

    public void btnvoltarClickAction(ActionEvent event) {
        Stage stageAtual = (Stage) btnvoltar.getScene().getWindow();
        stageAtual.close();
    }

    public void Inicar() {
        try {
            // Carrega o novo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/planet-maker-dialog.fxml"));
            Parent root = loader.load();

            stage.setTitle("PLANET MAKER");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}