package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("unused")
public class ConfiguracoesController {
    @FXML
    private Slider sldvolume;

    @FXML
    private Button btnvoltar;


    private Stage arg08;

    public ConfiguracoesController() {

    }

    public ConfiguracoesController(Stage stage) {
        arg08 = stage;
        arg08.initStyle(StageStyle.UNDECORATED);
    }

    public void btnsalvarClickAction(ActionEvent event){
        float volume = (float) (sldvolume.getValue());

        String filePath = "src\\controller\\volume.txt";
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(String.valueOf((float)volume));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnvoltarClickAction(ActionEvent event){
        Stage stageprimario = (Stage)btnvoltar.getScene().getWindow();
        Window owner = stageprimario.getOwner();

        stageprimario.close();

    }

}
