package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Corpo;
import model.SendJsonAPI;
import model.Simulacao;

public class UniversosController {
    private static int quantidadeUniversos = 0;

    private Stage stage = new Stage();
    @FXML
    private FlowPane FlowUniversos;

    @FXML
    Button btnvoltar;

    public UniversosController(Stage arg0) {
        stage = arg0;
    }

    public UniversosController() {

    }

    public void btnuniversemakerClickAction() {
        if (quantidadeUniversos >= 5) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Já tem 5 simulações, remova uma para poder criar outra.");
            alert.showAndWait();
        } else {
            try {
                @SuppressWarnings("unused")
                UniversoMakerController universomakercontroller = new UniversoMakerController(stage);

                // Carrega o novo FXML

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/universo-maker-dialog.fxml"));
                Parent root = loader.load();

                stage.setTitle("Universo maker");
                stage.setScene(new Scene(root));
                stage.show();
                Image image = new Image("/assets/icon.png");
                stage.getIcons().add(image);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void btnvoltarClickAction(ActionEvent event) {
        Stage stageprimario = (Stage) btnvoltar.getScene().getWindow();
        stageprimario.close();
    }

    public void stageStyle() {
        stage.initStyle(StageStyle.UNDECORATED);
    }

    public void carregarSimulacoes() {
        String caminhoatual = System.getProperty("user.dir");
        String filelogin = caminhoatual + File.separator + "config" + File.separator + "login.txt";
        List<Simulacao> simulacoes = PegarUniversos(readLoginFromFile(filelogin));

        quantidadeUniversos = simulacoes.size();

        for (Simulacao simulacao : simulacoes) {
            FlowPane flow = new FlowPane();
            flow.setOrientation(Orientation.HORIZONTAL);
            flow.setPrefWrapLength(125);
            flow.setAlignment(Pos.CENTER);
            flow.setStyle(
                    "-fx-border-color: white;" +
                    "-fx-text-fill: black;"+
                    "-fx-text-alignment: center;"+
                            "-fx-border-width: 3px;" +
                            "-fx-border-radius: 20px;" +
                            "-fx-background-color: black;" +
                            "-fx-background-radius: 20px;" +
                            "-fx-padding: 10px;" +
                            "-fx-min-height: 150px;" +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.2), 8, 0.3, 0, 2);");

            Label nomeSimulacao = new Label(simulacao.getName());

            nomeSimulacao.setTextFill(Color.WHITE);
            nomeSimulacao.setTextAlignment(TextAlignment.CENTER);

            nomeSimulacao.setFont(new Font("Arial", 18));

            nomeSimulacao.setMaxWidth(125);
            nomeSimulacao.setPrefWidth(125);

            Button botaoIniciar = new Button("Iniciar");

            botaoIniciar.setFont(new Font("Arial", 18));

            botaoIniciar.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-text-fill: black;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-family: 'Arial Black', sans-serif;" +
                            "-fx-padding: 5px 10px;" +
                            "-fx-border-radius: 5px;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 6, 0.1, 0, 2);" +
                            "-fx-cursor: hand;");

            botaoIniciar.setMaxWidth(125);
            botaoIniciar.setPrefWidth(125);

            botaoIniciar.setOnAction(event -> iniciarSimulacao(simulacao));

            flow.getChildren().addAll(nomeSimulacao, botaoIniciar);

            FlowUniversos.getChildren().add(flow);
        }
    }

    public String readLoginFromFile(String filePath) {
        String text = null;

        try (BufferedReader leitorFiler = new BufferedReader(new FileReader(filePath))) {
            text = leitorFiler.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    private void iniciarSimulacao(Simulacao simulacao) {
        System.out.println("Iniciando simulação: " + simulacao);
        SendJsonAPI sendJsonAPI = new SendJsonAPI();
        List<Corpo> corposurl = sendJsonAPI
                .GetCorposList("http://localhost:3000/corpos/simulation/" + simulacao.getId());
        SimulacaoController simulacaoController = new SimulacaoController(stage, simulacao.getName());
        simulacaoController.IniciarSimulacaoSalva(simulacao, corposurl);
    }

    public List<Simulacao> PegarUniversos(String cookie) {
        List<Simulacao> simulacoes = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3000/simulation/" + cookie))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();

                if (responseBody.startsWith("[") && responseBody.endsWith("]")) {
                    responseBody = responseBody.substring(1, responseBody.length() - 1).trim();

                    String[] simulacoesArray = responseBody.split("},\\s*\\{");

                    for (int i = 0; i < simulacoesArray.length; i++) {
                        if (!simulacoesArray[i].startsWith("{")) {
                            simulacoesArray[i] = "{" + simulacoesArray[i];
                        }
                        if (!simulacoesArray[i].endsWith("}")) {
                            simulacoesArray[i] = simulacoesArray[i] + "}";
                        }

                        String cor = extrairValor(simulacoesArray[i], "cor");
                        System.out.println("cor é:" + cor);
                        String nome = extrairValor(simulacoesArray[i], "nome");
                        String id_simulacao = extrairValor(simulacoesArray[i], "id");

                        int idSimulacao = 0;
                        try {
                            if (id_simulacao != null && !id_simulacao.isEmpty()) {
                                idSimulacao = Integer.parseInt(id_simulacao);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Erro ao converter ID da simulação: " + id_simulacao);
                        }

                        Simulacao simulacao = new Simulacao(nome, cor, cookie, idSimulacao);
                        System.out.println(simulacao.getName());
                        System.out.println(simulacao.getCor());
                        System.out.println(simulacao.getCookie_user());
                        System.out.println(simulacao.getId());
                        simulacoes.add(simulacao);
                    }
                }
            } else {
                System.out.println("Erro na requisição. Código de resposta: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return simulacoes;
    }

    private String extrairValor(String json, String chave) {
        String chaveComAspas = "\"" + chave + "\"";
        int startIndex = json.indexOf(chaveComAspas);

        if (startIndex == -1) {
            return null;
        }

        startIndex = json.indexOf(":", startIndex) + 1;
        if (startIndex == 0) {
            return null;
        }

        char startChar = json.charAt(startIndex);

        int endIndex;
        if (startChar == '"') {
            startIndex++;
            endIndex = json.indexOf("\"", startIndex);
        } else if (startChar == '{') {
            endIndex = json.indexOf("}", startIndex) + 1;
        } else {
            endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = json.indexOf("}", startIndex);
            }
        }

        if (endIndex == -1) {
            return null;
        }

        String valor = json.substring(startIndex, endIndex).trim();

        if (valor.startsWith("\"") && valor.endsWith("\"")) {
            valor = valor.substring(1, valor.length() - 1);
        }

        return valor;
    }

}
