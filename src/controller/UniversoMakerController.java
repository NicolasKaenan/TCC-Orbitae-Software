package controller;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UniversoMakerController {
    @FXML
    private TextField tfnome;

    @FXML
    private Button btncriar;

    @FXML
    private Button btnvoltar;

    @FXML
    private Rectangle retbackground;

    @FXML
    private ColorPicker cpbackgroundcolor;

    private File filechooser_image;

    private Stage stage = new Stage();

    public UniversoMakerController() {

    }

    public UniversoMakerController(Stage arg0) {
        stage = arg0;
    }

    public void btnvoltarClickAction(ActionEvent event) {
        Stage stageprimario = (Stage) btnvoltar.getScene().getWindow();
        stageprimario.close();
    }

    public void btncriarClickAction(ActionEvent event) {
        try {
            SimulacaoController simulacaoController = new SimulacaoController(stage, tfnome.getText(), new StackPane());
            if (filechooser_image == null) {
                simulacaoController.setBackgroundcolor(cpbackgroundcolor.getValue());
            }else{
                simulacaoController.setImage(filechooser_image);
            }
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

    public void Escolhercor(ActionEvent event) {
        retbackground.setFill(cpbackgroundcolor.getValue());
    }

    public void FileChooser(ActionEvent event) {
        javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
        fc.getExtensionFilters().add(new ExtensionFilter("JPG", "*.jpg"));
        filechooser_image = fc.showOpenDialog(null);
    }

    public void stageStyle() {
        stage.initStyle(StageStyle.UNDECORATED);
    }

}
