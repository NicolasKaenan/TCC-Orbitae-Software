package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

public class UniversoMakerController {
    @FXML 
    private TextField tfnome;

    @FXML
    private Button btncriar;

    @FXML
    private Button btnvoltar;
    
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

    public void btnvoltarClickAction(ActionEvent event){
        try {
            @SuppressWarnings("unused")
            UniversosController universoscontroller = new UniversosController(stage);
            // Carrega o novo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/universos.fxml"));
            Parent root = loader.load();

            stage.setTitle("Universos");
            stage.setScene(new Scene(root));
            stage.show();
            Stage stagePrincipal = (Stage) btnvoltar.getScene().getWindow();
            stagePrincipal.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
}
}
