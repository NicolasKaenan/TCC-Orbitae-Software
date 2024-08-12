package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class TelainicialController {
    @FXML 
    private Button btnuniversos;
    private Stage stage = new Stage();

    public TelainicialController() {
        
    }


    public TelainicialController(Stage arg8) {
        stage = arg8;
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
            stagePrincipal.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnconfiguracoesClickAction(ActionEvent event) {
        
    }

    public void btnsairClickAction(ActionEvent event) {
        Platform.exit();
    }
}
