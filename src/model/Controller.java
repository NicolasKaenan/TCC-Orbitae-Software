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
    private final DoubleProperty angleX = new SimpleDoubleProperty(0); // Ângulo de rotação no eixo X
    private final DoubleProperty angleY = new SimpleDoubleProperty(0); // Ângulo de rotação no eixo Y
    private double anchorX, anchorY; // Posições iniciais do mouse
    private double mouseSensitivity = 0.1;  // Sensibilidade do mouse
    private boolean isMousePressed = false; // Verifica se o mouse está pressionado

    public Controller() {}

    public void ControlSimulation(SmartGroup grupo, Scene cena, Stage stage, Camera camera, List<Corpo> corpos) {
        // Transformações de rotação para a câmera
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        camera.getTransforms().addAll(xRotate, yRotate);

        // Vincula as propriedades de rotação aos eixos
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        // Movimentação do mouse para controle FPS
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

                // Atualiza os ângulos de rotação da câmera com base no movimento do mouse
                angleY.set(angleY.get() + deltaX * mouseSensitivity);
                angleX.set(angleX.get() - deltaY * mouseSensitivity);

                // Limita a rotação no eixo X para evitar virar de cabeça para baixo
                angleX.set(Math.max(-90, Math.min(90, angleX.get())));
            }
        });

        // Movimentação da câmera com teclado
        double moveSpeed = 20.0;  // Velocidade de movimento
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

        // Movimentação da câmera com o scroll do mouse
        double scrollSpeed = 2.0;  // Velocidade do scroll
        cena.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            double sinY = Math.sin(Math.toRadians(angleY.get()));
            double cosY = Math.cos(Math.toRadians(angleY.get()));

            // Atualiza a posição da câmera para frente ou para trás
            cameraTranslate.setX(cameraTranslate.getX() - sinY * delta * scrollSpeed);
            cameraTranslate.setZ(cameraTranslate.getZ() + cosY * delta * scrollSpeed);
        });

        // Exibindo o diálogo de criação de planeta ao clicar com o botão direito do mouse
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
