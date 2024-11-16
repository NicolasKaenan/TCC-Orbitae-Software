package model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class SendJsonAPI {

    public SendJsonAPI() {

    }

    public void sendJson(String jsonString, URI uri) {
        try {
            // Converta a URI para URL
            URL url = uri.toURL();

            // Configure a conexão
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Verifique o conteúdo da jsonString antes de enviá-la
            System.out.println("JSON enviado: " + jsonString);

            // Envie o JSON no corpo da requisição
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonString.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            // Leia a resposta da API
            int status = conn.getResponseCode();
            System.out.println("Código de status da resposta: " + status);

            InputStream inputStream = (status >= 400) ? conn.getErrorStream() : conn.getInputStream();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Resposta da API: " + response.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
