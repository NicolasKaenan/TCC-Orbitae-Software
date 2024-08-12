package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class UniversoMakerController {
    @FXML 
    private TextField tfnome;

    @FXML
    private Button btncriar;
    
    private Stage stage = new Stage();
    public UniversoMakerController(){

    }

    public UniversoMakerController(Stage arg0){
        stage = arg0;
    }

    public void btncriarClickAction(ActionEvent event){
        try {
            SimulacaoController simulacaoController = new SimulacaoController(stage, tfnome.getText());

            simulacaoController.iniciarSimulacao();
            Image image = new Image("/assets/icon.png");
            stage.getIcons().add(image);
            stage.show();
            Stage stagePrincipal = (Stage) btncriar.getScene().getWindow();
            stagePrincipal.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
