package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Controller;
import model.Corpo;
import model.SendJsonAPI;
import model.Simulacao;
import model.SimulacaoTimers;
import model.SmartGroup;

public class SimulacaoController {
    private int contador = 0;
    private static final double G = 120e-2;
    private final int WIDTH = 1350;
    private final int HEIGHT = 680;
    private String nome;
    static String filepath;
    private Stage stage;
    private Color backgroundcolor;
    private double anchorX, anchorY;
    private Camera camera = new PerspectiveCamera();
    private double anchorAngleX = 0, anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0), angleY = new SimpleDoubleProperty(0);
    private List<Corpo> corpos = new ArrayList<>();
    private SmartGroup grupo = new SmartGroup();
    private SimulacaoTimers simulacaoTimers = new SimulacaoTimers(corpos);

    @FXML
    Button btnvoltar;
    @FXML
    TextField tfnome;
    @FXML
    TextField tfraio;
    @FXML
    TextField tfmassa;
    @FXML
    ColorPicker cpcor;
    @FXML
    Sphere sphere;

    public SimulacaoController(Stage arg0, String nome) {
        stage = arg0;
        stage.setResizable(false);
        this.nome = nome;
        backgroundcolor = Color.WHITE;
    }

    public void iniciarSimulacao() {
        try {
            String caminhoatual = System.getProperty("user.dir");
            String filelogin = caminhoatual + File.separator + "config" + File.separator + "login.txt";
            String texto = readFromFile(filelogin);
            Simulacao simulacao = new Simulacao(nome, backgroundcolor.toString(), texto);
            SendJsonAPI sendJsonAPI = new SendJsonAPI();
            System.out.println(simulacao.CreateJson());

            URI uri = new URI("http://localhost:3000/simulation");
            sendJsonAPI.sendJson(simulacao.CreateJson(), uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Corpo corpo1 = new Corpo(20.0, "1", 20, camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ(),
                0, 0, 0);
        Corpo corpo2 = new Corpo(20.0, "2", 20, camera.getTranslateX() + 100, camera.getTranslateY(),
                camera.getTranslateZ(), 0, 0, 0);
        corpos.add(corpo1);
        corpos.add(corpo2);
        grupo.getChildren().addAll(corpos);
        grupo.translateXProperty().set(WIDTH / 2);
        grupo.translateYProperty().set(HEIGHT / 2);
        grupo.translateZProperty().set(-2000);

        Scene cena = new Scene(grupo, WIDTH, HEIGHT, true);
        cena.setFill(backgroundcolor);

        cena.setCamera(camera);

        camera.setLayoutX(camera.getLayoutX() * 10);
        camera.setLayoutY(camera.getLayoutY() * 10);

        Controller controller = new Controller();
        controller.ControlSimulation(grupo, cena, stage, camera, corpos);

        AnimationTimer gravidade = simulacaoTimers.createGravidadeTimer();
        AnimationTimer colisao = simulacaoTimers.createColisaoTimer();

        gravidade.start();
        colisao.start();

        stage.setScene(cena);
        Image image = new Image("/assets/icon.png");
        stage.getIcons().add(image);
        stage.setTitle("simulação " + nome);
        stage.show();
    }

    public SimulacaoController() {

    }

    public String readFromFile(String filePath) {
        String text = null;

        try (BufferedReader leitorFiler = new BufferedReader(new FileReader(filePath))) {
            text = leitorFiler.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    public void stageStyle() {
        stage.initStyle(StageStyle.UNDECORATED);
    }

    public boolean colisao(Corpo one, Corpo two) {

        Bounds one_borda = one.getBoundsInParent();
        Bounds two_borda = two.getBoundsInParent();

        return one_borda.intersects(two_borda);
    }

    public void btncriarClickAction(ActionEvent event) {
        if (tfnome.getText().isEmpty() || tfmassa.getText().isEmpty() || tfraio.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Preencha tudo.");
            alert.showAndWait();
            return;
        }
        // Aqui ta o problema
        String nome = tfnome.getText();
        Double massa = Double.valueOf(tfmassa.getText());
        int raio = Integer.valueOf(tfraio.getText());
        String cor = cpcor.getValue().toString();

        // Onde eu crio o novo corpo com as especificações do usuário
        Corpo corpon = new Corpo(massa, nome, raio, camera.getTranslateX(), camera.getTranslateY(),
                camera.getTranslateZ(), 0, 0, 0);

        corpon.Colorir(cor);

        corpos.add(corpon);
        corpon.setCullFace(CullFace.BACK);
        grupo.getChildren().add(corpon);

        Stage stageAtual = (Stage) btnvoltar.getScene().getWindow();
        stageAtual.close();
    }

    public void btnvoltarClickAction(ActionEvent event) {
        Stage stageAtual = (Stage) btnvoltar.getScene().getWindow();
        stageAtual.close();
    }

    public void Escolhadecor(ActionEvent event) {
        String cor = cpcor.getValue().toString();
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.valueOf(cor));
        sphere.setMaterial(material);
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public static double getG() {
        return G;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public List<Corpo> getCorpos() {
        return corpos;
    }

    public void setCorpos(List<Corpo> corpos) {
        this.corpos = corpos;
    }

    public double getAnchorX() {
        return anchorX;
    }

    public void setAnchorX(double anchorX) {
        this.anchorX = anchorX;
    }

    public double getAnchorY() {
        return anchorY;
    }

    public void setAnchorY(double anchorY) {
        this.anchorY = anchorY;
    }

    public double getAnchorAngleX() {
        return anchorAngleX;
    }

    public void setAnchorAngleX(double anchorAngleX) {
        this.anchorAngleX = anchorAngleX;
    }

    public double getAnchorAngleY() {
        return anchorAngleY;
    }

    public void setAnchorAngleY(double anchorAngleY) {
        this.anchorAngleY = anchorAngleY;
    }

    public DoubleProperty getAngleX() {
        return angleX;
    }

    public DoubleProperty getAngleY() {
        return angleY;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Button getBtnvoltar() {
        return btnvoltar;
    }

    public void setBtnvoltar(Button btnvoltar) {
        this.btnvoltar = btnvoltar;
    }

    public TextField getTfnome() {
        return tfnome;
    }

    public void setTfnome(TextField tfnome) {
        this.tfnome = tfnome;
    }

    public TextField getTfraio() {
        return tfraio;
    }

    public void setTfraio(TextField tfraio) {
        this.tfraio = tfraio;
    }

    public TextField getTfmassa() {
        return tfmassa;
    }

    public void setTfmassa(TextField tfmassa) {
        this.tfmassa = tfmassa;
    }

    public ColorPicker getCpcor() {
        return cpcor;
    }

    public void setCpcor(ColorPicker cpcor) {
        this.cpcor = cpcor;
    }

    public Color getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(Color backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
    }

    public Sphere getSphere() {
        return sphere;
    }

    public void setSphere(Sphere sphere) {
        this.sphere = sphere;
    }
}