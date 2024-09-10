package controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
import model.SimulacaoTimers;
import model.SmartGroup;

public class SimulacaoController {
    private int contador = 0;
    // private static final double G = 6.67430e-2;
    private static final double G = 120e-2;
    private final int WIDTH = 1350;
    private final int HEIGHT = 680;
    private String nome;
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
        
        AnimationTimer colisao = simulacaoTimers.createColisaoTimer();
        AnimationTimer gravidade = simulacaoTimers.createGravidadeTimer();
        colisao.start();
        gravidade.start();

        
        stage.setScene(cena);
        Image image = new Image("/assets/icon.png");
        stage.getIcons().add(image);
        stage.setTitle("simulação " + nome);
        stage.show();
    }

    public SimulacaoController() {

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

        String nome = tfnome.getText();
        Double massa = Double.valueOf(tfmassa.getText());
        int raio = Integer.valueOf(tfraio.getText());
        String cor = cpcor.getValue().toString();

        Corpo corpon = new Corpo(massa,nome,raio, 0, 0 ,0, 0,0,0);

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
    // Array com alguns nomes de cores em inglês
    private static final String[] COLOR_NAMES = {
            "RED", "GREEN", "BLUE", "YELLOW", "CYAN", "MAGENTA", "ORANGE", "PINK", "GRAY", "DARKGRAY"
    };

    // Função para obter uma cor aleatória
    public static String getRandomColor() {
        Random random = new Random();
        // Escolha um nome de cor aleatório
        String colorName = COLOR_NAMES[random.nextInt(COLOR_NAMES.length)];
        // Retorna a cor correspondente
        return colorName;
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

    public static String[] getColorNames() {
        return COLOR_NAMES;
    }
}