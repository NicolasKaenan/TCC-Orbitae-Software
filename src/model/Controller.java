package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Camera;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.util.List;

public class Controller {
    private final DoubleProperty angleX = new SimpleDoubleProperty(0); 
    private final DoubleProperty angleY = new SimpleDoubleProperty(0); 
    private double anchorX, anchorY; 
    private double mouseSensitivity = 0.1;
    private boolean isMousePressed = false; 

    public Controller() {}

    public void ControlSimulation(SmartGroup grupo, Scene cena, Stage stage, Camera camera, List<Corpo> corpos) {
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        camera.getTransforms().addAll(xRotate, yRotate);

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        cena.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
                isMousePressed = true;
            }
        });

        cena.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                isMousePressed = false;
            }
        });

        cena.setOnMouseDragged(event -> {
            if (isMousePressed) {
                double deltaX = event.getSceneX() - anchorX;
                double deltaY = event.getSceneY() - anchorY;
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();

                angleY.set(angleY.get() + deltaX * mouseSensitivity);
                angleX.set(angleX.get() - deltaY * mouseSensitivity);

                angleX.set(Math.max(-90, Math.min(90, angleX.get())));
            }
        });

        double moveSpeed = 50.0; 
        Translate cameraTranslate = new Translate();
        camera.getTransforms().add(cameraTranslate);

        cena.setOnKeyPressed(event -> {
            double sinY = Math.sin(Math.toRadians(angleY.get()));
            double cosY = Math.cos(Math.toRadians(angleY.get()));

            if (event.getCode() == KeyCode.W) {
                cameraTranslate.setX(cameraTranslate.getX() - sinY * moveSpeed);
                cameraTranslate.setZ(cameraTranslate.getZ() + cosY * moveSpeed);
            }
            if (event.getCode() == KeyCode.S) {
                cameraTranslate.setX(cameraTranslate.getX() + sinY * moveSpeed);
                cameraTranslate.setZ(cameraTranslate.getZ() - cosY * moveSpeed);
            }
            if (event.getCode() == KeyCode.A) {
                cameraTranslate.setX(cameraTranslate.getX() - cosY * moveSpeed);
                cameraTranslate.setZ(cameraTranslate.getZ() - sinY * moveSpeed);
            }
            if (event.getCode() == KeyCode.D) {
                cameraTranslate.setX(cameraTranslate.getX() + cosY * moveSpeed);
                cameraTranslate.setZ(cameraTranslate.getZ() + sinY * moveSpeed);
            }
        });

        double scrollSpeed = 2.0; 
        cena.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            double sinY = Math.sin(Math.toRadians(angleY.get()));
            double cosY = Math.cos(Math.toRadians(angleY.get()));

            
            cameraTranslate.setX(cameraTranslate.getX() - sinY * delta * scrollSpeed);
            cameraTranslate.setZ(cameraTranslate.getZ() + cosY * delta * scrollSpeed);
        });


        cena.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                Platform.runLater(() -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/planet-maker-dialog.fxml"));
                    try {
                        Parent root = loader.load();
                        Stage dialogStage = new Stage();
                        dialogStage.setTitle("Planet Maker");
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        dialogStage.initOwner(stage.getOwner());
                        dialogStage.setScene(new Scene(root));
                        dialogStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
