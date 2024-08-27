package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class TelainicialController {
    @FXML 
    private Button btnuniversos;
    private Stage stage = new Stage();
    public WavPlayer player = new WavPlayer();

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
            Image image = new Image("/assets/icon.png");
            stage.getIcons().add(image);
            stage.show();
            Stage stagePrincipal = (Stage) btnuniversos.getScene().getWindow();
            
            stagePrincipal.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnconfiguracoesClickAction(ActionEvent event) {
        try{
        @SuppressWarnings("unused")
            ConfiguracoesController configuracoescontroller = new ConfiguracoesController(stage);
            // Carrega o novo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/configuracoes.fxml"));
            Parent root = loader.load();

            stage.setTitle("configuracoes");
            stage.setScene(new Scene(root));
            stage.show();
            Stage stagePrincipal = (Stage) btnuniversos.getScene().getWindow();
            player.StopMusic();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void btnsairClickAction(ActionEvent event) {
        Platform.exit();
    }
}
