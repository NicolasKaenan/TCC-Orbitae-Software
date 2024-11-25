package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.HttpServerLogin;
import model.Simulacao;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@SuppressWarnings("unused")
public class TelainicialController {
    @FXML
    private VBox vboxUniversos;
    @FXML
    private Button btnuniversos;
    private Stage stage = new Stage();
    public WavPlayer player = new WavPlayer();

    public TelainicialController() {

    }

    public TelainicialController(Stage arg8) {
        stage = arg8;
        arg8.initStyle(StageStyle.UNDECORATED);
    }

    @FXML
    public void btnuniversosClickAction(ActionEvent event) {
        try {
            String caminhoatual = System.getProperty("user.dir");
            String filelogin = caminhoatual + File.separator + "config" + File.separator + "login.txt";


            @SuppressWarnings("unused")
        
            // Carrega o novo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/universos.fxml"));

            
            Parent root = loader.load();

            UniversosController universosController = loader.getController();

            universosController.carregarSimulacoes();
            
            stage.setTitle("Universos");
            stage.setScene(new Scene(root));
            stage.show();
            Stage stagePrincipal = (Stage) btnuniversos.getScene().getWindow();
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readLoginFromFile(String filePath) {
        String text = null;

        try (BufferedReader leitorFiler = new BufferedReader(new FileReader(filePath))) {
            text = leitorFiler.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    public void Iniciar() {
        this.stage.show();
    }

    public void btnconfiguracoesClickAction(ActionEvent event) {
        try {
            // Carrega o novo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/configuracoes.fxml"));
            Parent root = loader.load();
            ConfiguracoesController configuracoesController = loader.getController();

            configuracoesController.iniciarVolume();

            stage.setTitle("configuracoes");
            stage.setScene(new Scene(root));
            stage.show();
            @SuppressWarnings("unused")
            Stage stagePrincipal = (Stage) btnuniversos.getScene().getWindow();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stageStyle() {
        stage.initStyle(StageStyle.UNDECORATED);
    }

    public void btnsairClickAction(ActionEvent event) {
        Platform.exit();
    }
}
