package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UniversosController {
    private Stage stage = new Stage();

    @FXML
    Button btnvoltar;
    
    public UniversosController(Stage arg0) {
        stage = arg0;
    }

    public UniversosController() {

    }

    public void btnuniversemakerClickAction() {
        try {
            @SuppressWarnings("unused")
            UniversoMakerController universomakercontroller = new UniversoMakerController(stage);

            // Carrega o novo FXML

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/universo-maker-dialog.fxml"));
            Parent root = loader.load();

            stage.setTitle("Universo maker");
            stage.setScene(new Scene(root));
            stage.show();
            Image image = new Image("/assets/icon.png");
            stage.getIcons().add(image);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnvoltarClickAction(ActionEvent event){
        Stage stageprimario = (Stage)btnvoltar.getScene().getWindow();
        stageprimario.close();
    }

    public void stageStyle(){
        stage.initStyle(StageStyle.UNDECORATED);
    }
}
