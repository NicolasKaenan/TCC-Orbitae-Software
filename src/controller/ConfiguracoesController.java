package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;

public class ConfiguracoesController {
    @FXML
    private Slider sldvolume;


    private Stage arg08;

    public ConfiguracoesController() {

    }

    public ConfiguracoesController(Stage stage) {
        arg08 = stage;
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

}
