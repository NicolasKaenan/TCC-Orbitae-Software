import java.io.IOException;

import controller.TelainicialController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage arg0) throws Exception {
        try {
            // Carrega o novo FXML
            Parent p = FXMLLoader.load(getClass().getResource("/view/tela-inicial.fxml"));
            Scene cena = new Scene(p);
            @SuppressWarnings("unused")
            TelainicialController telainicialController = new TelainicialController(arg0);
            arg0.setScene(cena);
            arg0.setTitle("Orbitae");
            Image image = new Image("/assets/icon.png");
            arg0.getIcons().add(image);
            arg0.show();
        } catch (IOException e) {
            e.printStackTrace(); // Tratar o erro de maneira apropriada, não mecher em nada aqui
        }
    }
}
