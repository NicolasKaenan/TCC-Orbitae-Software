package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
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
    private static Stage stage;
    private Color backgroundcolor;
    private double anchorX, anchorY;
    private Camera camera = new PerspectiveCamera();
    private double anchorAngleX = 0, anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0), angleY = new SimpleDoubleProperty(0);
    private static List<Corpo> corpos = new ArrayList<>();
    private static SmartGroup grupo = new SmartGroup();
    private static SimulacaoTimers simulacaoTimers = new SimulacaoTimers(corpos);
    private AnimationTimer colisao = simulacaoTimers.createColisaoTimer();
    private AnimationTimer gravidade = simulacaoTimers.createGravidadeTimer();
    private Controller controller = new Controller();
    private static Simulacao simulacao = new Simulacao();
    private Scene cena;

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
            simulacao = new Simulacao(nome, backgroundcolor.toString(), texto);
            SendJsonAPI sendJsonAPI = new SendJsonAPI();
            System.out.println(simulacao.CreateJson());

            URI uri = new URI("http://localhost:3000/simulation");
            int resposta = sendJsonAPI.sendJson(simulacao.CreateJson(), uri);
            simulacao.setId(resposta);
            System.out.println(resposta);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    URI uricorpos;
                    try {
                        uricorpos = new URI("http://localhost:3000/corpos/" + simulacao.getId());
                        sendJsonAPI.sendCorpos(corpos, uricorpos);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    gravidade.stop();
                    cena.setRoot(new SmartGroup());
                    colisao.stop();
                    corpos.clear();
                    grupo.getChildren().clear();
                    System.err.println("acabou a simulação");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Corpo corpo1 = new Corpo(2000.0, "Sol", 200, camera.getTranslateX() - 60, camera.getTranslateY(),
                camera.getTranslateZ() + 644,
                0, 0, 0);
        Corpo corpo2 = new Corpo(500.0, "jupiter", 100, camera.getTranslateX() + 200, camera.getTranslateY() + 600,
                camera.getTranslateZ(),
                0, 0, 0);
        Corpo corpo3 = new Corpo(200.0, "terra", 50, camera.getTranslateX() - 800, camera.getTranslateY() - 900,
                camera.getTranslateZ() - 900,
                0, 0, 0);

        corpo1.Colorir(Color.valueOf("WHITE"));
        corpo2.Colorir(Color.valueOf("RED"));
        corpo3.Colorir(Color.valueOf("BLUE"));
        corpos.add(corpo1);
        corpos.add(corpo2);
        corpos.add(corpo3);
        grupo.getChildren().addAll(corpos);
        grupo.translateXProperty().set(WIDTH / 2);
        grupo.translateYProperty().set(HEIGHT / 2);
        grupo.translateZProperty().set(-2000);

        cena = new Scene(grupo, WIDTH, HEIGHT, true);
        cena.setFill(backgroundcolor);

        cena.setCamera(camera);

        camera.setLayoutX(camera.getLayoutX() * 10);
        camera.setLayoutY(camera.getLayoutY() * 10);
        camera.setTranslateZ(camera.getTranslateZ()+740);

        controller.ControlSimulation(grupo, cena, stage, camera, corpos);

        colisao.start();
        gravidade.start();

        stage.setScene(cena);
        Image image = new Image("/assets/icon.png");
        stage.getIcons().add(image);
        stage.setTitle("simulação " + nome);
        stage.show();
    }

    private void ReiniciarSimulacao() {
        cena = stage.getScene();
        SendJsonAPI sendJsonAPI = new SendJsonAPI();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                URI uricorpos;
                try {
                    uricorpos = new URI("http://localhost:3000/corpos/" + simulacao.getId());
                    sendJsonAPI.sendCorpos(corpos, uricorpos);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                gravidade.stop();
                cena.setRoot(new SmartGroup());
                colisao.stop();
                corpos.clear();
                grupo.getChildren().clear();
                System.err.println("acabou a simulação");
            }
        });
        grupo.getChildren().clear();
        grupo.getChildren().addAll(corpos);

        // Reiniciar as animações
        colisao.stop();
        colisao.start();
        gravidade.stop();
        gravidade.start();
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
        } else {
            String nome = tfnome.getText();
            Double massa = Double.valueOf(tfmassa.getText());
            int raio = Integer.parseInt(tfraio.getText());
            Color cor = cpcor.getValue();

            Corpo corpon = new Corpo(massa, nome, raio, camera.getTranslateX(), camera.getTranslateY(),
                    camera.getTranslateZ(), 0, 0, 0);
            corpon.Colorir(cor);
            corpos.add(corpon);
            grupo.getChildren().addAll(corpon);
            System.out.println(grupo.getChildren().contains(corpon));

            Stage stageAtual = (Stage) btnvoltar.getScene().getWindow();
            stageAtual.close();
            System.out.println("Corpo criado: " + corpon.getNome());
            System.out.println("Corpo massa: " + corpon.getMassa());
            System.out.println("Corpo raio: " + corpon.getRaio());
            System.out.println("Total de corpos: " + corpos.size());

            ReiniciarSimulacao();
        }
    }

    public void IniciarSimulacaoSalva(Simulacao simulacaoSalva, List<Corpo> corpossalvos) {
        System.out.println(corpossalvos.size());
        String corHex = simulacaoSalva.getCor(); 
        if (corHex.startsWith("0x")) {
            corHex = "#" + corHex.substring(2);
        }
        setBackgroundcolor(Color.web(corHex));
        
        setSimulacao(simulacaoSalva);
        grupo = new SmartGroup();
        corpos.clear();
        corpos.addAll(corpossalvos);
        grupo.getChildren().addAll(corpos);
        cena = new Scene(grupo, WIDTH, HEIGHT, true);
        setBackgroundcolor(Color.web(simulacaoSalva.getCor()));
        cena.setFill(backgroundcolor);
        controller.ControlSimulation(grupo, cena, stage, camera, corpos);
        stage.setScene(cena);
        camera.setLayoutX(camera.getLayoutX() * 10);
        camera.setLayoutY(camera.getLayoutY() * 10);
        camera.setTranslateZ(camera.getTranslateZ()+740);
        gravidade.start();
        colisao.start();
        
        SendJsonAPI sendJsonAPI = new SendJsonAPI();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                URI uricorpos;
                try {
                    uricorpos = new URI("http://localhost:3000/corpos/" + simulacao.getId());
                    sendJsonAPI.sendCorpos(corpos, uricorpos);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                gravidade.stop();
                cena.setRoot(new SmartGroup());
                colisao.stop();
                corpos.clear();
                grupo.getChildren().clear();
                System.err.println("acabou a simulação");
            }
        });
        cena.setCamera(camera);

        camera.setLayoutX(camera.getLayoutX() * 10);
        camera.setLayoutY(camera.getLayoutY() * 10);

        colisao.start();
        gravidade.start();

        stage.setScene(cena);
        Image image = new Image("/assets/icon.png");
        stage.getIcons().add(image);
        stage.setTitle("simulação " + nome);
        stage.show();
    }

    public void btnvoltarClickAction(ActionEvent event) {
        Stage stageAtual = (Stage) btnvoltar.getScene().getWindow();
        stageAtual.close();
        ReiniciarSimulacao();
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

    public List<Corpo> getCorpos() {
        return corpos;
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

    public static String getFilepath() {
        return filepath;
    }

    public static void setFilepath(String filepath) {
        SimulacaoController.filepath = filepath;
    }

    public static SmartGroup getGrupo() {
        return grupo;
    }

    public static void setGrupo(SmartGroup grupo) {
        SimulacaoController.grupo = grupo;
    }

    public SimulacaoTimers getSimulacaoTimers() {
        return simulacaoTimers;
    }

    public AnimationTimer getColisao() {
        return colisao;
    }

    public void setColisao(AnimationTimer colisao) {
        this.colisao = colisao;
    }

    public AnimationTimer getGravidade() {
        return gravidade;
    }

    public void setGravidade(AnimationTimer gravidade) {
        this.gravidade = gravidade;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public static Simulacao getSimulacao() {
        return simulacao;
    }

    public static void setSimulacao(Simulacao simulacao) {
        SimulacaoController.simulacao = simulacao;
    }
}