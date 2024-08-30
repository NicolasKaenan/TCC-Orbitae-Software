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

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

    public void iniciarVolume(){
        InputStream in = getClass().getResourceAsStream("/controller/volume.txt");
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        try {
            sldvolume.setValue(Double.valueOf(input.readLine()));
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public ConfiguracoesController(Stage stage) {
        arg08 = stage;
        arg08.setResizable(false);
    }

    public void btnsalvarClickAction(ActionEvent event){
        // float volume = (float) (sldvolume.getValue());

        // InputStream in = getClass().getResourceAsStream("/controller/volume.txt");
        // // Tente criar o BufferedWriter e escrever no arquivo
        // try (BufferedWriter writer = new BufferedWriter(in)) {
        //     // Escreva uma linha de texto no arquivo
        //     writer.write("Este é um exemplo de escrita com BufferedWriter.");
        //     writer.newLine(); // Adiciona uma nova linha
        //     writer.write("Aqui está outra linha de texto.");
        // } catch (IOException e) {
        //     e.printStackTrace(); // Tratamento de exceção em caso de erro
        // }
    }

    public void btnvoltarClickAction(ActionEvent event){
        Stage stageprimario = (Stage)btnvoltar.getScene().getWindow();
        Window owner = stageprimario.getOwner();

        stageprimario.close();

    }


}
