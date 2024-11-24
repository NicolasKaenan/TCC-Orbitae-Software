package model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SendJsonAPI {

    public SendJsonAPI() {

    }

    public int sendJson(String jsonString, URI uri) {
        try {
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            System.out.println("JSON enviado: " + jsonString);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonString.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            int status = conn.getResponseCode();
            System.out.println("Código de status da resposta: " + status);

            InputStream inputStream = (status >= 400) ? conn.getErrorStream() : conn.getInputStream();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                String responseBody = response.toString();
                System.out.println("Resposta da API: " + responseBody);

                int idStart = response.indexOf("\"id\":") + 5;
                int idEnd = response.indexOf("}", idStart);
                if (idStart > 4 && idEnd > idStart) {
                    String idString = response.substring(idStart, idEnd).trim();
                    int id = Integer.parseInt(idString);
                    System.out.println("ID extraído: " + id);
                    return id;
                } else {
                    System.out.println("Erro ao localizar o ID no JSON.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Corpo> GetCorposList(String urlString) {
        List<Corpo> corposList = new ArrayList<>();

        try {
            URI uri = new URI(urlString);
            HttpURLConnection conn = (HttpURLConnection) (uri.toURL()).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int status = conn.getResponseCode();
            System.out.println("Código de status da resposta: " + status);

            if (status >= 200 && status < 300) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    String responseBody = response.toString();
                    System.out.println("Resposta da API: " + responseBody);

                    String[] jsonObjects = responseBody.substring(1, responseBody.length() - 1).split("\\},\\{");
                    for (String jsonObject : jsonObjects) {
                        String sanitizedJson = jsonObject.replaceAll("[\\[\\]{}\"]", "");
                        String[] properties = sanitizedJson.split(",");

                        Corpo corpo = new Corpo();
                        for (String property : properties) {
                            String[] keyValue = property.split(":");
                            String key = keyValue[0].trim();
                            String value = keyValue[1].trim();

                            switch (key) {
                                case "id":
                                    corpo.setId(Integer.parseInt(value));
                                    break;
                                case "nome":
                                    corpo.setNome(value);
                                    break;
                                case "id_simulacao":
                                    break;
                                case "massa":
                                    corpo.setMassa(Double.parseDouble(value));
                                    break;
                                case "cor":
                                    corpo.ColorirCorSalva(value);
                                    break;
                                case "position_x":
                                    corpo.setX(Double.parseDouble(value));
                                    break;
                                case "position_y":
                                    corpo.setY(Double.parseDouble(value));
                                    break;
                                case "position_z":
                                    corpo.setZ(Double.parseDouble(value));
                                    break;
                                case "velocidade_x":
                                    corpo.SetVelocidadeX(Double.parseDouble(value));
                                    break;
                                case "velocidade_y":
                                    corpo.SetVelocidadeY(Double.parseDouble(value));
                                    break;
                                case "velocidade_z":
                                    corpo.SetVelocidadeZ(Double.parseDouble(value));
                                    break;
                            }
                        }
                        corposList.add(corpo);
                    }
                }
            } else {
                System.out.println("Erro ao obter corpos da API. Status: " + status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return corposList;
    }

    public void sendCorpos(List<Corpo> corpos, URI uri) {
        try {
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");
            for (int i = 0; i < corpos.size(); i++) {
                Corpo corpo = corpos.get(i);
                jsonBuilder.append("{")
                        .append("\"id\":").append(corpo.GetId()).append(",")
                        .append("\"massa\":").append(corpo.getMassa()).append(",")
                        .append("\"position_x\":").append(corpo.getX()).append(",")
                        .append("\"position_y\":").append(corpo.getY()).append(",")
                        .append("\"position_z\":").append(corpo.getZ()).append(",")
                        .append("\"nome\":\"").append(corpo.getNome()).append("\",")
                        .append("\"raio\":").append(corpo.getRaio()).append(",")
                        .append("\"velocidade_x\":").append(corpo.GetVelocidadeX()).append(",")
                        .append("\"velocidade_y\":").append(corpo.GetVelocidadeY()).append(",")
                        .append("\"velocidade_z\":").append(corpo.GetVelocidadeZ()).append(",")
                        .append("\"cor\":\"").append(corpo.getCor()).append("\"")
                        .append("}");

                if (i < corpos.size() - 1) {
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("]");
            String jsonString = jsonBuilder.toString();
            System.out.println(jsonString);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonString.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

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
