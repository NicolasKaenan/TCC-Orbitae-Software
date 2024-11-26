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
                        Relatorio relatorio = new Relatorio();

                        for (String property : properties) {
                            String[] keyValue = property.split(":");
                            if (keyValue.length < 2)
                                continue;
                            String key = keyValue[0].trim();
                            String value = keyValue[1].trim();

                            switch (key) {
                                case "id":
                                    corpo.setId(Integer.parseInt(value));
                                    relatorio.setIdCorpo(Integer.parseInt(value));
                                    break;
                                case "nome":
                                    corpo.setNome(value);
                                    relatorio.setNomeCorpo(value);
                                    break;
                                case "massa":
                                    corpo.setMassa(Double.parseDouble(value));
                                    relatorio.setMassa(Double.parseDouble(value));
                                    break;
                                case "raio":
                                    corpo.setRaio(Double.parseDouble(value));
                                    relatorio.setRaio(Double.parseDouble(value));
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
                                    corpo.setVelocidadeX(Double.parseDouble(value));
                                    relatorio.setVelocidadeMediaX(Double.parseDouble(value));
                                    break;
                                case "velocidade_y":
                                    corpo.setVelocidadeY(Double.parseDouble(value));
                                    relatorio.setVelocidadeMediaY(Double.parseDouble(value));
                                    break;
                                case "velocidade_z":
                                    corpo.setVelocidadeZ(Double.parseDouble(value));
                                    relatorio.setVelocidadeMediaZ(Double.parseDouble(value));
                                    break;
                                case "cor":
                                    corpo.Colorir(value);
                                    relatorio.setCor(value);
                                    break;
                            }

                            if (key.startsWith("relatorio")) {
                                String relKey = key.replace("relatorio.", "");
                                switch (relKey) {
                                    case "id":
                                        relatorio.setId(Integer.parseInt(value));
                                        break;
                                    case "quantidadeColisoes":
                                        relatorio.setQuantidadeColisoes(Integer.parseInt(value));
                                        break;
                                    case "densidade":
                                        relatorio.setDensidade(Double.parseDouble(value));
                                        break;
                                    case "volume":
                                        relatorio.setVolume(Double.parseDouble(value));
                                        break;
                                    case "nomeCorpo":
                                        relatorio.setNomeCorpo(value);
                                        break;
                                    case "massa":
                                        relatorio.setMassa(Double.parseDouble(value));
                                        break;
                                    case "raio":
                                        relatorio.setRaio(Double.parseDouble(value));
                                        break;
                                    case "velocidadeMediaX":
                                        relatorio.setVelocidadeMediaX(Double.parseDouble(value));
                                        break;
                                    case "velocidadeMediaY":
                                        relatorio.setVelocidadeMediaY(Double.parseDouble(value));
                                        break;
                                    case "velocidadeMediaZ":
                                        relatorio.setVelocidadeMediaZ(Double.parseDouble(value));
                                        break;
                                }
                            }
                        }
                        Corpo corponovo = new Corpo(corpo.getMassa(), corpo.getNome(), corpo.getRaio(), corpo.getX(),
                                corpo.getY(), corpo.getZ(), corpo.getVelocidadeX(), corpo.GetVelocidadeY(),
                                corpo.GetVelocidadeZ());
                        corponovo.setRelatorio(relatorio);
                        corponovo.Colorir(corpo.getCor());
                        corponovo.setId(corpo.GetId());
                        corposList.add(corponovo);
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
                Relatorio relatorio = corpo.getRelatorio();

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
                        .append("\"cor\":\"").append(corpo.getCor()).append("\",")

                        .append("\"relatorio\":{")
                        .append("\"id\":").append(relatorio.getId()).append(",")
                        .append("\"idCorpo\":").append(relatorio.getIdCorpo()).append(",")
                        .append("\"nomeCorpo\":\"").append(relatorio.getNomeCorpo()).append("\",")
                        .append("\"massa\":").append(relatorio.getMassa()).append(",")
                        .append("\"densidade\":").append(relatorio.getDensidade()).append(",")
                        .append("\"volume\":").append(relatorio.getVolume()).append(",")
                        .append("\"raio\":").append(relatorio.getRaio()).append(",")
                        .append("\"quantidadeColisoes\":").append(relatorio.getQuantidadeColisoes()).append(",")
                        .append("\"velocidadeMediaX\":").append(relatorio.getVelocidadeMediaX()).append(",")
                        .append("\"velocidadeMediaY\":").append(relatorio.getVelocidadeMediaY()).append(",")
                        .append("\"velocidadeMediaZ\":").append(relatorio.getVelocidadeMediaZ()).append(",")
                        .append("\"cor\":\"").append(relatorio.getCor()).append("\"")
                        .append("}")
                        .append("}");

                if (i < corpos.size() - 1) {
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("]");

            String jsonString = jsonBuilder.toString();
            System.out.println("JSON enviado: " + jsonString);

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
