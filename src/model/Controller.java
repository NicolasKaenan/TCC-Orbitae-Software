package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Camera;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class Controller {
    private final DoubleProperty angleX = new SimpleDoubleProperty(0), angleY = new SimpleDoubleProperty(0);
    private double anchorX, anchorY;
    private double anchorAngleX = 0, anchorAngleY = 0;
    private int contador = 0;


    public Controller(){
        
    }

    public void ControlSimulation(SmartGroup grupo, Scene cena, Stage arg0, Camera camera, List<Corpo> corpos) {
        Rotate xRotate;
        Rotate yRotate;
        camera.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS));
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        arg0.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            camera.translateZProperty().set(camera.getTranslateZ() + delta);
        });

        cena.setOnZoomStarted(KeyEventevent -> {

        });

        arg0.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            System.out.println(event.getCode());
            if (event.getCode().toString() == "A") {
                camera.translateXProperty().set(camera.getTranslateX() - 20);
            }
            if (event.getCode().toString() == "D") {
                camera.translateXProperty().set(camera.getTranslateX() + 20);
            }
            if (event.getCode().toString() == "W") {
                camera.translateYProperty().set(camera.getTranslateY() - 20);
            }
            if (event.getCode().toString() == "S") {
                camera.translateYProperty().set(camera.getTranslateY() + 20);
            }
        });

        cena.setOnMousePressed(event -> {

            if (event.getButton() == MouseButton.SECONDARY) {
                anchorX = event.getX();
                anchorY = event.getY();
                anchorAngleX = angleX.get();
                anchorAngleY = angleY.get();
            } else if (event.getButton() == MouseButton.PRIMARY) {
                // Carrega o novo FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/planet-maker-dialog.fxml"));

                Parent root;
                try {
                    root = loader.load();
                    // Configuração do diálogo
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("Planet Maker");
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.initOwner(arg0.getOwner());
                    dialogStage.setScene(new Scene(root));
                    dialogStage.showAndWait();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                double mouseX = event.getX();
                double mouseY = event.getY();
                Corpo newC = new Corpo(1.0, "12", 100, mouseX, mouseY, camera.getTranslateZ() + 10, 0, 0, 0);
                // newC.Colorir(getRandomColor());
                newC.setCullFace(CullFace.BACK);
                contador += 1;

                corpos.add(newC);

                grupo.getChildren().add(newC);
                System.out.println(contador);
            }

            System.out.println(camera.getTranslateZ());
            System.out.println(camera.getTranslateZ());

        });

        cena.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX + (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY - (anchorX - event.getSceneX()));
        });

    }
}
