import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import com.sun.net.httpserver.HttpServer;
import controller.TelainicialController;
import controller.WavPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.HttpServerLogin;

public class App extends Application {
    public WavPlayer player = new WavPlayer();
    private CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            HttpServerLogin httpServer = new HttpServerLogin(latch);

            HttpServer server = httpServer.CreateSimpleHttpServer();
            String caminhoatual = System.getProperty("user.dir");
            String filelogin = caminhoatual + File.separator + "config" + File.separator + "login.txt";

            String login = httpServer.readLoginFromFile(filelogin);
            System.out.println(login);
            System.out.println(filelogin);

            if (login != null) {
                iniciarInterfacePrincipal(stage); // Se o login for válido, inicia a interface principal
            } else {
                // Inicia o servidor e aguarda a resposta
                server.start();
                System.out.println("Servidor rodando na porta 8080...");

                latch.await(); // Aguarda até que o latch seja liberado
                server.stop(0);
                iniciarInterfacePrincipal(stage); // Após a resposta, inicia a interface principal
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); // Tratar o erro de maneira apropriada, não mexer em nada aqui
        }
    }

    // Método que inicia a interface principal
    private void iniciarInterfacePrincipal(Stage stage) throws IOException {
        stage.initStyle(StageStyle.UNDECORATED);
        String caminhoatual = System.getProperty("user.dir");
        String filevolume = caminhoatual + File.separator + "config" + File.separator + "volume.txt";
        String musica_url = "/resources/music/music.wav";

        float volume = player.readVolumeFromFile(filevolume);
        player.playWavFile(musica_url, volume);

        Parent root = FXMLLoader.load(getClass().getResource("/view/tela-inicial.fxml"));
        Scene scene = new Scene(root);
        new TelainicialController(stage); // Inicializa o controlador

        stage.setScene(scene);
        stage.setTitle("Orbitae");
        Image image = new Image("/assets/icon.png");
        stage.getIcons().add(image);
        stage.show();
    }
}
