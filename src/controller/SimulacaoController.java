package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import model.Corpo;
import model.SmartGroup;

public class SimulacaoController {
    private int contador = 0;
    // private static final double G = 6.67430e-2;
    private static final double G = 20e-2;
    private final int WIDTH = 1350;
    private final int HEIGHT = 680;
    private String nome;
    private Stage stage;
    private List<Corpo> corpos = new ArrayList<>();
    private double anchorX, anchorY;
    private double anchorAngleX = 0, anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0), angleY = new SimpleDoubleProperty(0);

    public SimulacaoController(Stage arg0, String nome) {
        stage = arg0;
        this.nome = nome;
    }

    public void iniciarSimulacao() {
        SmartGroup grupo = new SmartGroup();
        grupo.translateXProperty().set(WIDTH / 2);
        grupo.translateYProperty().set(HEIGHT / 2);
        grupo.translateZProperty().set(-2000);

        Scene cena = new Scene(grupo, WIDTH, HEIGHT, true);
        cena.setFill(Color.BLACK);

        Camera camera = new PerspectiveCamera();
        cena.setCamera(camera);

        camera.setLayoutX(camera.getLayoutX() * 10);
        camera.setLayoutY(camera.getLayoutY() * 10);
        initMouseControl(grupo, cena, stage, camera);

        AnimationTimer gravidade = new AnimationTimer() {
            @Override
            public void handle(long now) {
                List<Corpo> corpos2 = new ArrayList<>(corpos); // Crie uma cópia da lista corpos

                Iterator<Corpo> iterator = corpos2.iterator();
                while (iterator.hasNext()) {
                    Corpo c = iterator.next();
                    iterator.remove(); // Remove o elemento corrente usando o iterador
                    for (Corpo d : corpos2) {
                        atualizarPosicao(c, d);
                        System.out.println("Distancia entre " + c.getNome() + " e " + d.getNome() + Distancia(c, d));
                    }
                }

            }
        };
        gravidade.start();

        AnimationTimer colisao = new AnimationTimer() {
            @Override
            public void handle(long now) {
                List<Corpo> corpos2 = new ArrayList<>(corpos); // Crie uma cópia da lista corpos

                Iterator<Corpo> iterator = corpos2.iterator();
                while (iterator.hasNext()) {
                    Corpo c = iterator.next();
                    iterator.remove(); // Remove o elemento corrente usando o iterador
                    for (Corpo d : corpos2) {
                        if (colisao(c, d)) {
                            double v1_c = c.GetVelocidadeX() * c.getMassa() - d.GetVelocidadeX() * d.getMassa();
                            double v2_c = c.GetVelocidadeY() * c.getMassa() - d.GetVelocidadeY() * d.getMassa();
                            double v3_c = c.GetVelocidadeZ() * c.getMassa() - d.GetVelocidadeZ() * d.getMassa();

                            double v1_d = d.GetVelocidadeX() * d.getMassa() - c.GetVelocidadeX() * c.getMassa();
                            double v2_d = d.GetVelocidadeY() * d.getMassa() - c.GetVelocidadeY() * c.getMassa();
                            double v3_d = d.GetVelocidadeZ() * d.getMassa() - c.GetVelocidadeZ() * c.getMassa();

                            c.SetVelocidadeX(c.GetVelocidadeX() - (v1_c / c.getMassa()));
                            c.SetVelocidadeY(c.GetVelocidadeY() - (v2_c / c.getMassa()));
                            c.SetVelocidadeZ(c.GetVelocidadeZ() - (v3_c / c.getMassa()));

                            d.SetVelocidadeX(d.GetVelocidadeX() - (v1_d / d.getMassa()));
                            d.SetVelocidadeY(d.GetVelocidadeY() - (v2_d / d.getMassa()));
                            d.SetVelocidadeZ(d.GetVelocidadeZ() - (v3_d / d.getMassa()));

                        }
                    }
                }

            }
        };
        colisao.start();
        stage.setScene(cena);
        Image image = new Image("/assets/icon.png");
            stage.getIcons().add(image);
        stage.setTitle("simulação "+nome);
        stage.show();
    }

    public SimulacaoController() {

    }

    private void initMouseControl(SmartGroup grupo, Scene cena, Stage arg0, Camera camera) {
        Rotate xRotate;
        Rotate yRotate;
        grupo.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS));
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        arg0.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            camera.translateZProperty().set(camera.getTranslateZ() + delta);
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
                double mouseX = event.getX();
                double mouseY = event.getY();
                Corpo newC = new Corpo(1.0, "12", 100, mouseX, mouseY, camera.getTranslateZ() + 10, 0, 0, 0);
                newC.Colorir("BLUE");
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
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + (anchorX - event.getSceneX()));
        });

    }

    public boolean colisao(Corpo one, Corpo two) {

        Bounds one_borda = one.getBoundsInParent();
        Bounds two_borda = two.getBoundsInParent();

        return one_borda.intersects(two_borda);
    }

    private double DistanciaX(Corpo one, Corpo two) {
        double distancia = one.getTranslateX() - two.getTranslateX();
        return distancia;
    }

    private double DistanciaY(Corpo one, Corpo two) {
        double distancia = one.getTranslateY() - two.getTranslateY();
        return distancia;
    }

    private double Distancia(Corpo one, Corpo two) {
        double x = DistanciaX(one, two);
        double y = DistanciaY(one, two);
        return Math.sqrt((x * x) + (y * y));
    }

    private void atualizarPosicao(Corpo one, Corpo two) {
        Double forca = (one.getMassa() * two.getMassa()) * G / Distancia(one, two);
        Double aceleracao_one = forca / one.getMassa();
        Double aceleracao_two = forca / two.getMassa();

        if (one.getTranslateX() < two.getTranslateX()) {
            one.SetVelocidadeX(one.GetVelocidadeX() + aceleracao_one);
            two.SetVelocidadeX(two.GetVelocidadeX() - aceleracao_two);
        } else if (one.getTranslateX() > two.getTranslateX()) {
            one.SetVelocidadeX(one.GetVelocidadeX() - aceleracao_one);
            two.SetVelocidadeX(two.GetVelocidadeX() + aceleracao_two);
        }

        if (one.getTranslateY() < two.getTranslateY()) {
            one.SetVelocidadeY(one.GetVelocidadeY() + aceleracao_one);
            two.SetVelocidadeY(two.GetVelocidadeY() - aceleracao_two);
        } else if (one.getTranslateY() > two.getTranslateY()) {
            one.SetVelocidadeY(one.GetVelocidadeY() - aceleracao_one);
            two.SetVelocidadeY(two.GetVelocidadeY() + aceleracao_two);
        }

        if (one.getTranslateZ() < two.getTranslateZ()) {
            one.SetVelocidadeZ(one.GetVelocidadeZ() + aceleracao_one);
            two.SetVelocidadeZ(two.GetVelocidadeZ() - aceleracao_two);
        } else if (one.getTranslateZ() > two.getTranslateZ()) {
            one.SetVelocidadeZ(one.GetVelocidadeZ() - aceleracao_one);
            two.SetVelocidadeZ(two.GetVelocidadeZ() + aceleracao_two);
        }

        one.setTranslateX(one.getTranslateX() + one.GetVelocidadeX());
        one.setTranslateZ(one.getTranslateZ() + one.GetVelocidadeZ());
        one.setTranslateY(one.getTranslateY() + one.GetVelocidadeY());
        two.setTranslateX(two.getTranslateX() + two.GetVelocidadeX());
        two.setTranslateZ(two.getTranslateZ() + two.GetVelocidadeZ());
        two.setTranslateY(two.getTranslateY() + two.GetVelocidadeY());

    }

}
