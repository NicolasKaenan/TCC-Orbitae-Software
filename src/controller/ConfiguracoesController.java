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
import model.HttpServerLogin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("unused")
public class ConfiguracoesController {
    @FXML
    private Slider sldvolume;

    @FXML
    private Button btnvoltar;

    private Stage arg08;

    public ConfiguracoesController() {

    }

    public void iniciarVolume() {
        String caminhoatual = System.getProperty("user.dir");
        String filevolume = caminhoatual + File.separator + "config" + File.separator + "volume.txt";
        sldvolume.setValue(Double.valueOf(Float.parseFloat(readTextFromFile(filevolume))));      
    }

    public String readTextFromFile(String filePath) {
        String text = null;

        try (BufferedReader leitorFiler = new BufferedReader(new FileReader(filePath))) {
            text = leitorFiler.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    public ConfiguracoesController(Stage stage) {
        arg08 = stage;
        arg08.setResizable(false);
    }

    public void btnsalvarClickAction(ActionEvent event) {
        String caminhoatual = System.getProperty("user.dir");
        String filevolume = caminhoatual + File.separator + "config" + File.separator + "volume.txt";
        float volume = (float) (sldvolume.getValue());
        WriteTextFromFile(String.valueOf(volume), filevolume);
        WavPlayer wavPlayer = new WavPlayer();
        wavPlayer.VolumeAtual(volume);
    }

    public static void WriteTextFromFile(String text, String filePath){
        try(FileWriter escrever = new FileWriter(filePath)){
            escrever.write(text);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void btnvoltarClickAction(ActionEvent event) {
        Stage stageprimario = (Stage) btnvoltar.getScene().getWindow();
        Window owner = stageprimario.getOwner();

        stageprimario.close();

    }

}
